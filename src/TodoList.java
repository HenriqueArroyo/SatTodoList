import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Handler;

/**
 * TodoList
 */
public class TodoList extends JFrame {
    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton botaoAdicionar;
    private JList<String> tarefasList;
    private DefaultListModel<String> listModel;
    private JButton botaoDeletar;
    private JButton markDoneButton;
    private JComboBox<String> filterComboBox;
    private JButton clearCompletedButton;
    private List<Task> tarefas;








    
    // construtor
    public TodoList() {
        // Configuração da janela principal
        super("To-Do List App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 300);
        // Inicializa o painel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        // Inicializa a lista de tarefas e a lista de tarefas concluídas
        tarefas = new ArrayList<>();
        listModel = new DefaultListModel<>();
        tarefasList = new JList<>(listModel);

        // Inicializa campos de entrada, botões e JComboBox
        taskInputField = new JTextField();
        botaoAdicionar = new JButton("Adicionar");
        botaoDeletar = new JButton("Excluir");
        markDoneButton = new JButton("Concluir");
        filterComboBox = new JComboBox<>(new String[] { "Todas", "Ativas",
                "Concluídas" });
        clearCompletedButton = new JButton("Limpar Concluídas");
        // Configuração do painel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(botaoAdicionar, BorderLayout.EAST);
        // Configuração do painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(botaoDeletar);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(filterComboBox);
        buttonPanel.add(clearCompletedButton);
        // Adiciona os componentes ao painel principal
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tarefasList), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        // Adiciona o painel principal à janela
        this.add(mainPanel);
        // Tratamento de eventos da aplicação
       

         //Tratamento de evento do método normal
        botaoAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e ){
                 addTask(e);
            }    
        });


        //vamo luigiiii confio em vc
        // botaoAdicionar.addKeyListener(new KeyAdapter() {
        //     public void keyPressed(KeyEvent f ){
        //         if(f.getKeyCode() == KeyEvent.VK_ENTER){
        //             addTask(f);
        //         }
        //     }
        // });

          botaoDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e ){
                 deleteTask(e);
            }    
        });





    }

    // Configuração de Listener para os Eventos
    private void addTask(ActionEvent e) {
        // Adiciona uma nova task à lista de tarefas
        String taskDescription = taskInputField.getText().trim();// remove espaços vazios
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            tarefas.add(newTask);
            updateTaskList();
            taskInputField.setText("");
        }
    }

    private void deleteTask(ActionEvent e) {
        // Exclui a task selecionada da lista de tarefas
        int selectedIndex = tarefasList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tarefas.size()) {
            tarefas.remove(selectedIndex);
            updateTaskList();
        }
    }

    private void markTaskDone() {
        // Marca a task selecionada como concluída
        int selectedIndex = tarefasList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tarefas.size()) {
            Task task = tarefas.get(selectedIndex);
            task.setDone(true);
            updateTaskList();
        }
    }

    private void filterTasks() {
        // Filtra as tarefas com base na seleção do JComboBox
        String filter = (String) filterComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tarefas) {
            if (filter.equals("Todas") || (filter.equals("Ativas") &&
                    !task.isDone()) || (filter.equals("Concluídas") && task.isDone())) {
                listModel.addElement(task.getDescription());
            }
        }
    }

    private void clearCompletedTasks() {
        // Limpa todas as tarefas concluídas da lista
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tarefas) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }
        tarefas.removeAll(completedTasks);
        updateTaskList();
    }

    private void updateTaskList() {
        // Atualiza a lista de tarefas exibida na GUI
        listModel.clear();
        for (Task task : tarefas) {
            listModel.addElement(task.getDescription() + (task.isDone() ?

                    " (Concluída)" : ""));

        }
    }

    public void run() {
        // Exibe a janela
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new TodoList().run();
    }
}