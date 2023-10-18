import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TodoList extends JFrame {
    // atributos
    private JPanel panel;
    private JTextField tarefaField;
    private JButton botaoAdicionar;
    private JList<String> listaTarefas;
    private DefaultListModel<String> listModel;
    private JButton botaoDeletar;
    private JButton botaoConfirmar;
    private JComboBox<String> filtroComboBox;
    private JButton botaoLimpar;
    private JComboBox<String> categoriaComboBox;
    private List<Task> tasks;
    private JLabel categoria;
    private JLabel situacao;

    public TodoList() {
        // Configuração da janela principal
        super("To-Do List App");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        this.setSize(650, 400);
        this.setResizable(false);
        // Inicializa o painel principal
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Inicializa a lista de tasks e a lista de tasks concluídas
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        listaTarefas = new JList<>(listModel);

        // Inicializa campos de entrada, botões e JComboBox
        situacao = new JLabel("Situação:");
        categoria = new JLabel("Categoria:");
        tarefaField = new JTextField();
        botaoAdicionar = new JButton("Adicionar");
        botaoDeletar = new JButton("Excluir");
        botaoConfirmar = new JButton("Concluir");
        filtroComboBox = new JComboBox<>(new String[] { "Todas", "Ativas",
                "Concluídas" });
        categoriaComboBox = new JComboBox<>(new String[] { "Todas", "Lazer", "Estudo", "Trabalho" });
        botaoLimpar = new JButton("Limpar Concluídas");

        // Configuração do painel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(tarefaField, BorderLayout.CENTER);
        inputPanel.add(botaoAdicionar, BorderLayout.EAST);
        // Configuração do painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(botaoDeletar);
        buttonPanel.add(botaoConfirmar);
        buttonPanel.add(botaoLimpar);
        buttonPanel.add(situacao);
        buttonPanel.add(filtroComboBox);
        buttonPanel.add(categoria);
        buttonPanel.add(categoriaComboBox);
        // Adiciona os componentes ao painel principal
         panel.add(buttonPanel, BorderLayout.NORTH);
         panel.add(new JScrollPane(listaTarefas), BorderLayout.CENTER);
         panel.add(inputPanel, BorderLayout.SOUTH);
       
        // Adiciona o painel principal à janela
        this.add(panel);

        // Tratamento de Eventos da Aplicação (vocês)
        botaoAdicionar.addActionListener(e->{
            adicionarTarefa();
        });

        // Apertar Enter e adicionar uma tarefa
         tarefaField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                   adicionarTarefa();
                }
            }
        });

         botaoDeletar.addActionListener(e->{
            deletarTarefa();
        });

         botaoConfirmar.addActionListener(e->{
            concluirTarefa();
        });

         botaoLimpar.addActionListener(e->{
            limparConcluidas();
        });

        filtroComboBox.addActionListener(e->{
            filtroTarefas();
        });

        listaTarefas.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) { // Verifica se a tecla pressionada é "Delete"
                    deletarTarefa();
                }
            }
        });
    
        listaTarefas.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                   concluirTarefa();
                }
            }
        });

      categoriaComboBox.addActionListener(e->{
            filtroCategoria();
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                fecharAplicacao();
            }
        });
    }
        
    // métodos
    private void adicionarTarefa() {
   // Adicione uma nova task à lista de tasks
   String taskDescription = tarefaField.getText().trim();
   String selectedCategory = (String) categoriaComboBox.getSelectedItem(); // Obtém a categoria selecionada

   if (!taskDescription.isEmpty()) {
       Task newTask = new Task(taskDescription, selectedCategory); // Crie a tarefa com a categoria selecionada
       tasks.add(newTask);
       updatelistaTarefas();
       tarefaField.setText("");
   }
        }

    private void deletarTarefa() {
       // Exclui a task selecionada da lista de tasks
    int selectedIndex = listaTarefas.getSelectedIndex();
    if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir esta tarefa?", "Confirmação",
            JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            tasks.remove(selectedIndex);
            updatelistaTarefas();
        }
    }
    }

    private void concluirTarefa() {
        // Marca a task selecionada como concluída
        int selectedIndex = listaTarefas.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            Task task = tasks.get(selectedIndex);
            task.setDone(true);
            updatelistaTarefas();
        }
    }

    private void filtroTarefas() {
        // Filtra as tasks com base na seleção do JComboBox
        String filter = (String) filtroComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (filter.equals("Todas") || (filter.equals("Ativas") &&
                    !task.isDone()) || (filter.equals("Concluídas") && task.isDone())) {
                listModel.addElement(task.getDescription());
            }
        }
    }

    private void limparConcluidas() {
        // Limpa todas as tasks concluídas da lista
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }
        tasks.removeAll(completedTasks);
        updatelistaTarefas();
    }

    private void updatelistaTarefas() {
        // Atualiza a lista de tasks exibida na GUI
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription() + (task.isDone() ?

                    " (Concluída)" : ""));

        }
    }

    private void filtroCategoria() {
        String selectedCategory = (String) categoriaComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (selectedCategory.equals("Todas") || task.getCategory().equals(selectedCategory)) {
                listModel.addElement(task.getDescription() + (task.isDone() ? " (Concluída)" : ""));
            }
        }
    }

    private void fecharAplicacao() {
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do aplicativo?", "Confirmação",
            JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0); // Fecha o aplicativo
        }
    }

    public void run() {
        // Exibe a janela
        this.setVisible(true);
    }
}