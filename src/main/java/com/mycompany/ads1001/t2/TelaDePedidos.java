package com.mycompany.ads1001.t2;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TelaDePedidos extends JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(TelaDePedidos.class.getName());

    // --- Modelo ---
    private Pedido pedidoAtual = new Pedido();
    private DefaultTableModel tabelaModel;
    private static final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    // --- Componentes ---
    private JPanel painelTopo;
    private JPanel painelFormulario;
    private JPanel painelTabela;
    private JPanel painelRodape;

    private JLabel lblTitulo;
    private JLabel lblCliente;
    private JTextField txtCliente;
    private JLabel lblProduto;
    private JComboBox<Produto> cmbProdutos;
    private JLabel lblQuantidade;
    private JSpinner spnQuantidade;
    private JLabel lblPreco;
    private JLabel lblPrecoValor;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JTable tabelaPedidos;
    private JScrollPane scrollTabela;
    private JLabel lblTotal;
    private JLabel lblTotalValor;
    private JButton btnConfirmar;
    private JButton btnNovoPedido;

    public TelaDePedidos() {
        initComponents();
        configurarEstilo();
        carregarProdutos();
        atualizarTotal();
    }

    private void initComponents() {
        setTitle("Sistema de Pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        // === TOPO ===
        painelTopo = new JPanel();
        painelTopo.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        painelTopo.setBackground(new Color(20, 20, 30));

        lblTitulo = new JLabel("🏁  Sistema de Pedidos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        painelTopo.add(lblTitulo);

        // === FORMULÁRIO ===
        painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(new Color(240, 242, 245));
        painelFormulario.setBorder(new CompoundBorder(
                new EmptyBorder(16, 16, 8, 16),
                new TitledBorder(new LineBorder(new Color(180, 180, 200), 1, true),
                        " Novo Item ", TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(60, 60, 80))
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        lblCliente = criarLabel("Cliente:");
        painelFormulario.add(lblCliente, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCliente = new JTextField(25);
        estilizarCampo(txtCliente);
        painelFormulario.add(txtCliente, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        // Produto
        gbc.gridx = 0; gbc.gridy = 1;
        lblProduto = criarLabel("Produto:");
        painelFormulario.add(lblProduto, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbProdutos = new JComboBox<>();
        estilizarCombo(cmbProdutos);
        cmbProdutos.addActionListener(e -> atualizarPreco());
        painelFormulario.add(cmbProdutos, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        // Quantidade
        gbc.gridx = 0; gbc.gridy = 2;
        lblQuantidade = criarLabel("Quantidade:");
        painelFormulario.add(lblQuantidade, gbc);

        gbc.gridx = 1;
        spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spnQuantidade.setPreferredSize(new Dimension(80, 32));
        spnQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnQuantidade.addChangeListener(e -> atualizarPreco());
        painelFormulario.add(spnQuantidade, gbc);

        // Preço
        gbc.gridx = 2; gbc.gridy = 2;
        lblPreco = criarLabel("Preço unit.:");
        painelFormulario.add(lblPreco, gbc);

        gbc.gridx = 3;
        lblPrecoValor = new JLabel("R$ 0,00");
        lblPrecoValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPrecoValor.setForeground(new Color(30, 120, 60));
        painelFormulario.add(lblPrecoValor, gbc);

        // Botão Adicionar
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        btnAdicionar = criarBotao("+ Adicionar ao Pedido", new Color(37, 99, 235));
        btnAdicionar.addActionListener(e -> adicionarItem());
        painelFormulario.add(btnAdicionar, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;

        // === TABELA ===
        painelTabela = new JPanel(new BorderLayout(0, 6));
        painelTabela.setBackground(new Color(240, 242, 245));
        painelTabela.setBorder(new CompoundBorder(
                new EmptyBorder(0, 16, 8, 16),
                new TitledBorder(new LineBorder(new Color(180, 180, 200), 1, true),
                        " Itens do Pedido ", TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(60, 60, 80))
        ));

        tabelaModel = new DefaultTableModel(new String[]{"Produto", "Preço Unit.", "Qtd", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaPedidos = new JTable(tabelaModel);
        tabelaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaPedidos.setRowHeight(28);
        tabelaPedidos.setShowGrid(false);
        tabelaPedidos.setIntercellSpacing(new Dimension(0, 0));
        tabelaPedidos.setSelectionBackground(new Color(200, 220, 255));
        tabelaPedidos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaPedidos.getTableHeader().setBackground(new Color(37, 99, 235));
        tabelaPedidos.getTableHeader().setForeground(Color.WHITE);
        tabelaPedidos.getTableHeader().setReorderingAllowed(false);

        // Zebra stripes
        tabelaPedidos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) setBackground(r % 2 == 0 ? Color.WHITE : new Color(245, 247, 252));
                setBorder(new EmptyBorder(0, 8, 0, 8));
                if (c == 3) setHorizontalAlignment(SwingConstants.RIGHT);
                else setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

        scrollTabela = new JScrollPane(tabelaPedidos);
        scrollTabela.setPreferredSize(new Dimension(740, 200));
        scrollTabela.setBorder(new LineBorder(new Color(200, 200, 220)));

        btnRemover = criarBotao("🗑  Remover Selecionado", new Color(180, 40, 40));
        btnRemover.addActionListener(e -> removerItem());

        JPanel painelBotaoRemover = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        painelBotaoRemover.setOpaque(false);
        painelBotaoRemover.add(btnRemover);

        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelTabela.add(painelBotaoRemover, BorderLayout.SOUTH);

        // === RODAPÉ ===
        painelRodape = new JPanel(new BorderLayout());
        painelRodape.setBackground(new Color(20, 20, 30));
        painelRodape.setBorder(new EmptyBorder(12, 20, 12, 20));

        lblTotal = new JLabel("TOTAL DO PEDIDO:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(200, 200, 210));

        lblTotalValor = new JLabel(moeda.format(0));
        lblTotalValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalValor.setForeground(new Color(74, 222, 128));

        JPanel painelTotalTexto = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        painelTotalTexto.setOpaque(false);
        painelTotalTexto.add(lblTotal);
        painelTotalTexto.add(lblTotalValor);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);

        btnNovoPedido = criarBotao("↺  Novo Pedido", new Color(100, 100, 120));
        btnNovoPedido.addActionListener(e -> novoPedido());

        btnConfirmar = criarBotao("✔  Confirmar Pedido", new Color(22, 163, 74));
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConfirmar.setPreferredSize(new Dimension(190, 40));
        btnConfirmar.addActionListener(e -> confirmarPedido());

        painelBotoes.add(btnNovoPedido);
        painelBotoes.add(btnConfirmar);

        painelRodape.add(painelTotalTexto, BorderLayout.WEST);
        painelRodape.add(painelBotoes, BorderLayout.EAST);

        // === LAYOUT PRINCIPAL ===
        setLayout(new BorderLayout());
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(new Color(240, 242, 245));
        painelCentro.add(painelFormulario);
        painelCentro.add(painelTabela);

        add(painelCentro, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
    }

    // =========================================================
    // LÓGICA DE NEGÓCIO
    // =========================================================

    private void carregarProdutos() {
        List<Produto> produtos = Produto.listarTodos();
        cmbProdutos.removeAllItems();
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum produto encontrado no banco.\nVerifique a conexão e os dados cadastrados.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (Produto p : produtos) {
            cmbProdutos.addItem(p);
        }
        atualizarPreco();
    }

    private void atualizarPreco() {
        Produto p = (Produto) cmbProdutos.getSelectedItem();
        if (p != null) {
            lblPrecoValor.setText(moeda.format(p.getPreco()));
        }
    }

    private void adicionarItem() {
        Produto p = (Produto) cmbProdutos.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int qtd;
        try {
            qtd = (Integer) spnQuantidade.getValue();
            if (qtd <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemPedido item = new ItemPedido(p, qtd);
        pedidoAtual.adicionarItem(item);

        tabelaModel.addRow(new Object[]{
            p.getNome(),
            moeda.format(p.getPreco()),
            qtd,
            moeda.format(item.getSubtotal())
        });

        atualizarTotal();
        spnQuantidade.setValue(1);
    }

    private void removerItem() {
        int linha = tabelaPedidos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item da tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemPedido item = pedidoAtual.getItens().get(linha);
        pedidoAtual.removerItem(item);
        tabelaModel.removeRow(linha);
        atualizarTotal();
    }

    private void confirmarPedido() {
        String cliente = txtCliente.getText().trim();
        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            txtCliente.requestFocus();
            return;
        }

        if (pedidoAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um item ao pedido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Confirmar pedido de %s?\nTotal: %s", cliente, moeda.format(pedidoAtual.getTotal())),
                "Confirmar Pedido", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            pedidoAtual.salvarNoBanco(cliente);
            JOptionPane.showMessageDialog(this,
                    String.format("✔ Pedido #%d salvo com sucesso!\nTotal: %s",
                            pedidoAtual.getId(), moeda.format(pedidoAtual.getTotal())),
                    "Pedido Confirmado", JOptionPane.INFORMATION_MESSAGE);
            novoPedido();
        }
    }

    private void novoPedido() {
        pedidoAtual = new Pedido();
        tabelaModel.setRowCount(0);
        txtCliente.setText("");
        spnQuantidade.setValue(1);
        atualizarTotal();
        txtCliente.requestFocus();
    }

    private void atualizarTotal() {
        lblTotalValor.setText(moeda.format(pedidoAtual.getTotal()));
    }

    // =========================================================
    // HELPERS DE ESTILO
    // =========================================================

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(50, 50, 70));
        return l;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(cor);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(180, 36));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color original = cor;
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(original.brighter());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(original);
            }
        });
        return b;
    }

    private void estilizarCampo(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 210), 1, true),
                new EmptyBorder(4, 8, 4, 8)));
    }

    private void estilizarCombo(JComboBox<?> c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setBackground(Color.WHITE);
    }

    private void configurarEstilo() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                    break;
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.WARNING, "Nimbus não disponível", ex);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new TelaDePedidos().setVisible(true));
    }
}
