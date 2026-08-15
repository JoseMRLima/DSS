package uminho.dss.thegrill.business.ssrestaurantes;

import java.util.UUID;

/**
 * Representa o Chief Operating Officer (COO) do sistema.
 * Possui permissões elevadas para consulta de informações sensíveis.
 */
public class COO extends Funcionario {

    // construtores

    /**
     * Construtor de COO.
     *
     * @param codFuncionario Identificador único
     * @param nome Nome do COO
     * @param email Email do COO
     * @param senha Senha de acesso
     */
    public COO(UUID codFuncionario, String nome, String email, String senha) {
        super(codFuncionario, nome, email, senha);
    }

}