package uminho.dss.thegrill.business.ssrestaurantes;

import java.util.List;
import java.util.UUID;

/**
 * Interface que define os serviços disponibilizados pelo subsistema de Restaurantes.
 * Este subsistema é responsável pela gestão de informações dos
 * restaurantes, incluindo estatísticas e validação de permissões.
 */
public interface ISSRestaurantes {

    boolean login(String email, String senha);

    void logout();

    /**
     * Verifica se um funcionário tem permissões para consultar informações dos restaurantes.
     * @return true se tiver permissão, false caso contrário
     */
    boolean podeConsultarInformacoes();

    /**
     * Lista os indicadores de um restaurante específico.
     * @param codRestaurante Identificador do restaurante
     * @return Lista de objetos Indicador associados ao restaurante
     */
    List<Indicador> listarIndicadores(UUID codRestaurante);

    /**
     * Lista todos os restaurantes registados no sistema.
     * @return Lista de objetos Restaurante
     */
    List<Restaurante> listarRestaurantes();

}