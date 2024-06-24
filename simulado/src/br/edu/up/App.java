package br.edu.up;

import br.edu.up.modelos.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws FileNotFoundException {
        String pessoasFile = "src/Pessoas.csv";
        String enderecosFile = "src/Enderecos.csv";
        String outputFile = "PessoaComEndereco.csv";

        List<Pessoa> pessoas = new ArrayList<>();
        Map<Integer, List<Endereco>> enderecoMap = new HashMap<>();

        // Ler o arquivo Pessoas.csv
        Scanner leitorPessoas = new Scanner(new File(pessoasFile));
        while (leitorPessoas.hasNextLine()) {
            String line = leitorPessoas.nextLine();
            String[] data = line.split(";");
            int codigo = Integer.parseInt(data[0]);
            String nome = data[1];
            Pessoa pessoa = new Pessoa(codigo, nome, "", "");
            pessoas.add(pessoa);
        }
        leitorPessoas.close();

        // Ler o arquivo Enderecos.csv
        Scanner leitorEnderecos = new Scanner(new File(enderecosFile));
        while (leitorEnderecos.hasNextLine()) {
            String line = leitorEnderecos.nextLine();
            String[] data = line.split(";");
            String rua = data[0];
            String cidade = data[1];
            int pessoaId = Integer.parseInt(data[2]);
            Endereco endereco = new Endereco(rua, cidade, pessoaId);

            if (!enderecoMap.containsKey(pessoaId)) {
                enderecoMap.put(pessoaId, new ArrayList<>());
            }
            enderecoMap.get(pessoaId).add(endereco);
        }
        leitorEnderecos.close();

        // Escrever no arquivo PessoaComEndereco.csv
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            for (Pessoa pessoa : pessoas) {
                List<Endereco> enderecosPessoa = enderecoMap.get(pessoa.getCodigo());
                if (enderecosPessoa != null) {
                    for (Endereco endereco : enderecosPessoa) {
                        Pessoa pessoaComEndereco = new Pessoa(pessoa.getCodigo(), pessoa.getNome(), endereco.getRua(), endereco.getCidade());
                        writer.println(pessoaComEndereco.toString());
                    }
                } else {
                    writer.println(pessoa.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Arquivo " + outputFile + " gerado com sucesso.");
    }
}
