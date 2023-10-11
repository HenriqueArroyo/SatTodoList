import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorTarefasApp {
    private JFrame frame;
    private JList<String> tarefasList;
    private DefaultListModel<String> listModel;
    private JTextField novaTarefaField;
    private JComboBox<String> categoriaComboBox;
    private Map<String, ArrayList<String>> categorias;
    private JComboBox<String> filtroComboBox; // ComboBox para filtrar tarefas
    private ArrayList<String> tarefasConcluidas; // Lista de tarefas concluídas

    public GerenciadorTarefasApp() {
        // Configuração da janela principal
        frame = new JFrame("Gerenciador de Tarefas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        // Inicialização do modelo da lista e da lista em si
        listModel = new DefaultListModel<>();
        tarefasList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(tarefasList);

        // Campo de texto para adicionar novas tarefas
        novaTarefaField = new JTextField(20);

        // Botão para adicionar tarefa e seu ouvinte de evento
        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(new AdicionarTarefaListener());

        // Botão para excluir tarefa e seu ouvinte de evento
        JButton excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(new ExcluirTarefaListener());

        // Botão para marcar tarefa como concluída
        JButton concluirButton = new JButton("Concluir");
        concluirButton.addActionListener(new ConcluirTarefaListener());

        // ComboBox para filtrar tarefas (Ativas, Concluídas, Todas)
        String[] filtrosArray = { "Ativas", "Concluídas", "Todas" };
        filtroComboBox = new JComboBox<>(filtrosArray);
        filtroComboBox.addActionListener(new FiltrarTarefasListener());

        // Opções da caixa de seleção de categorias e seu ouvinte de evento
        String[] categoriasArray = { "Todas", "Trabalho", "Estudo", "Lazer" };
        categoriaComboBox = new JComboBox<>(categoriasArray);
        categoriaComboBox.addActionListener(new FiltrarPorCategoriaListener());

        // Inicialização do mapa de categorias
        categorias = new HashMap<>();
        for (String categoria : categoriasArray) {
            categorias.put(categoria, new ArrayList<>());
        }

        // Inicialização da lista de tarefas concluídas
        tarefasConcluidas = new ArrayList<>();

        // Configuração do painel inferior com os componentes
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nova Tarefa:"));
        panel.add(novaTarefaField);
        panel.add(adicionarButton);
        panel.add(excluirButton);
        panel.add(concluirButton); // Botão para marcar tarefa como concluída
        panel.add(new JLabel("Filtrar por Categoria:"));
        panel.add(categoriaComboBox);
        panel.add(new JLabel("Filtrar Tarefas:"));
        panel.add(filtroComboBox); // ComboBox para filtrar tarefas

        // Configuração do layout e adição dos componentes à janela
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(listScrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    // Ouvinte de evento para adicionar tarefas
    private class AdicionarTarefaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String novaTarefa = novaTarefaField.getText();
            String categoriaSelecionada = (String) categoriaComboBox.getSelectedItem();

            // Adiciona a tarefa ao modelo da lista
            listModel.addElement(novaTarefa);

            // Adiciona a tarefa à lista correspondente à categoria
            categorias.get(categoriaSelecionada).add(novaTarefa);

            // Limpa o campo de texto
            novaTarefaField.setText(null);
        }
    }

    // Ouvinte de evento para excluir tarefas
    private class ExcluirTarefaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = tarefasList.getSelectedIndex();
            if (selectedIndex != -1) {
                String tarefaRemovida = listModel.getElementAt(selectedIndex);
                String categoriaDaTarefa = null;

                // Encontra a categoria correspondente à tarefa removida
                for (Map.Entry<String, ArrayList<String>> entry : categorias.entrySet()) {
                    if (entry.getValue().contains(tarefaRemovida)) {
                        categoriaDaTarefa = entry.getKey();
                        break;
                    }
                }

                // Remove a tarefa do modelo da lista
                listModel.remove(selectedIndex);

                // Remove a tarefa da lista correspondente à categoria
                if (categoriaDaTarefa != null) {
                    categorias.get(categoriaDaTarefa).remove(tarefaRemovida);
                }

                // Remove a tarefa da lista de tarefas concluídas, se aplicável
                if (tarefasConcluidas.contains(tarefaRemovida)) {
                    tarefasConcluidas.remove(tarefaRemovida);
                }
            }
        }
    }

    // Ouvinte de evento para marcar uma tarefa como concluída
    private class ConcluirTarefaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = tarefasList.getSelectedIndex();
            if (selectedIndex != -1) {
                String tarefa = listModel.getElementAt(selectedIndex);

                // Marca a tarefa como concluída
                if (!tarefa.endsWith(" (Concluída)")) {
                    listModel.setElementAt(tarefa + " (Concluída)", selectedIndex);
                    tarefasConcluidas.add(tarefa);
                }
            }
        }
    }

    // Ouvinte de evento para filtrar tarefas (Ativas, Concluídas, Todas)
    private class FiltrarTarefasListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            FiltrarTarefas();
        }
    }

    // Método para filtrar e exibir tarefas com base na seleção do filtro
    private void FiltrarTarefas() {
        String filtroSelecionado = (String) filtroComboBox.getSelectedItem();
        listModel.clear();

        if (filtroSelecionado.equals("Todas")) {
            // Exibe todas as tarefas
            for (ArrayList<String> tarefas : categorias.values()) {
                for (String tarefa : tarefas) {
                    listModel.addElement(tarefa);
                }
            }
        } else if (filtroSelecionado.equals("Ativas")) {
            // Exibe apenas tarefas não concluídas
            for (String categoria : categorias.keySet()) {
                for (String tarefa : categorias.get(categoria)) {
                    if (!tarefasConcluidas.contains(tarefa)) {
                        listModel.addElement(tarefa);
                    }
                }
            }
        } else if (filtroSelecionado.equals("Concluídas")) {
            // Exibe apenas tarefas marcadas como concluídas
            for (String tarefa : tarefasConcluidas) {
                listModel.addElement(tarefa + " (Concluída)");
            }
        }
    }

    // Ouvinte de evento para filtrar tarefas por categoria
    private class FiltrarPorCategoriaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String categoriaSelecionada = (String) categoriaComboBox.getSelectedItem();

            // Limpa o modelo da lista
            listModel.clear();

            // Exibe todas as tarefas correspondentes à categoria selecionada
            if (categoriaSelecionada.equals("Todas")) {
                for (ArrayList<String> tarefas : categorias.values()) {
                    for (String tarefa : tarefas) {
                        listModel.addElement(tarefa);
                    }
                }
            } else {
                for (String tarefa : categorias.get(categoriaSelecionada)) {
                    listModel.addElement(tarefa);
                }
            }
        }
    }

    // Método para exibir a janela
    public void exibir() {
        frame.setVisible(true);
    }

    // Método principal para executar o aplicativo
    public static void main(String[] args) {
        GerenciadorTarefasApp app = new GerenciadorTarefasApp();
        app.exibir();
    }
}
