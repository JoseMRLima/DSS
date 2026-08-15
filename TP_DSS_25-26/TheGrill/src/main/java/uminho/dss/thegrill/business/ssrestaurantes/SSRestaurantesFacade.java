package uminho.dss.thegrill.business.ssrestaurantes;

import uminho.dss.thegrill.business.ssrestaurantes.exceptions.FuncionarioInexistenteException;
import uminho.dss.thegrill.business.ssrestaurantes.exceptions.FuncionarioNaoAutenticadoException;
import uminho.dss.thegrill.business.ssrestaurantes.exceptions.RestauranteInexistenteException;
import uminho.dss.thegrill.data.FuncionarioDAO;
import uminho.dss.thegrill.data.IndicadorDAO;
import uminho.dss.thegrill.data.RestauranteDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Facade do subsistema de Restaurantes.
 */
public class SSRestaurantesFacade implements ISSRestaurantes {

    // variáveis de instância

    private Map<UUID, Funcionario> funcionarios;
    private Map<UUID, Restaurante> restaurantes;
    private Map<UUID, Indicador> indicadores;
    private List<UUID> codsRestaurantes;
    private List<UUID> codsFuncionarios;
    private List<UUID> codsIndicadores;
    private Funcionario funcionarioAtual;


    // construtores

    /**
     * Construtor da Facade.
     */
    public SSRestaurantesFacade() {
        this.funcionarios = FuncionarioDAO.getInstance();
        this.restaurantes = RestauranteDAO.getInstance();
        this.indicadores = IndicadorDAO.getInstance();
        this.codsRestaurantes = new ArrayList<>();
        this.codsFuncionarios = new ArrayList<>();
        this.codsIndicadores = new ArrayList<>();
        this.funcionarioAtual = null;
    }


    // métodos de instância

    public boolean login(String email, String senha) {
        for (UUID codF : this.codsFuncionarios) {
            Funcionario f = this.funcionarios.get(codF);
            if (f.getEmail().equals(email)) {
                this.funcionarioAtual = f;
                break;
            }
        }

        return this.funcionarioAtual != null;
    }

    public void logout() {
        this.funcionarioAtual = null;
    }

    /**
     * Verifica se o Funcionario pode consultar informações de Restaurantes
     * @return {@code true} se poder consultar, {@code false} caso contrário
     * @throws FuncionarioInexistenteException
     */
    @Override
    public boolean podeConsultarInformacoes() throws FuncionarioNaoAutenticadoException {
        if (this.funcionarioAtual == null)
            throw new FuncionarioNaoAutenticadoException("Funcionário não autenticado");

        return this.funcionarioAtual instanceof COO || this.funcionarioAtual instanceof Gerente;
    }

    /**
     * Devolve os Indicadores de um Restaurante
     * @param codRestaurante Identificador do restaurante
     * @return lista de Indicadores
     * @throws RestauranteInexistenteException
     */
    @Override
    public List<Indicador> listarIndicadores(UUID codRestaurante) throws RestauranteInexistenteException {
        Restaurante rest = this.restaurantes.get(codRestaurante);
        if (rest == null) {
            throw new RestauranteInexistenteException("Restaurante inexistente com o ID: " + codRestaurante);
        }
        return rest.listarIndicadores();
    }

    /**
     * Lista todos os restaurantes registados no sistema.
     * @return Lista de objetos Restaurante
     */
    public List<Restaurante> listarRestaurantes() throws FuncionarioNaoAutenticadoException {
        if (this.funcionarioAtual == null)
            throw new FuncionarioNaoAutenticadoException("Funcionário não autenticado");

        List<Restaurante> out = new ArrayList<>();

        if (this.funcionarioAtual instanceof Gerente) {
            // Gerente tem apenas acesso a um Restaurante
            Gerente g = (Gerente) this.funcionarioAtual;
            out.add(g.getRestaurante());
        } else if (this.funcionarioAtual instanceof COO) {
            // COO tem acesso a todos os restaurantes
            for (UUID codR : this.codsRestaurantes) {
                out.add(this.restaurantes.get(codR));
            }
        }

        return out;
    }

}