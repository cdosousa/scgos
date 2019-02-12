/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.zteste.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;

/**
 *
 * @author cristiano
 */
public class Tela extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JScrollPane scrlTree = null;
    private JTree treeLivros = null;
    private TreeModel treeModel;

    public Tela(TreeModel treeModel) {
        super();
        this.treeModel = treeModel;
        initialize();
    }

    private void initialize() {
        this.setSize(361, 240);
        this.setLocationRelativeTo(null);
        this.setTitle("Tree");
        this.setContentPane(getJContentPane());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getScrlTree(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JScrollPane getScrlTree() {
        if (scrlTree == null) {
            scrlTree = new JScrollPane();
            scrlTree.setViewportView(getTreeLivros());
        }
        return scrlTree;
    }

    private JTree getTreeLivros() {
        if (treeLivros == null) {
            treeLivros = new JTree(treeModel);
            treeLivros.setRootVisible(false);
            treeLivros.setShowsRootHandles(true);
        }
        return treeLivros;
    }

    public static void main(String[] args) {
        List<Livro> livros = new ArrayList<Livro>();

        Livro livro = new Livro("Duna");
        livro.addAutor(new Autor("Frank Herbert"));
        livros.add(livro);

        Livro livro2 = new Livro("50 Anos Depois");
        livro2.addAutor(new Autor("Chico Xavier"));
        livro2.addAutor(new Autor("Emmanuel (Espírito)"));
        livros.add(livro2);

        Livro livro3 = new Livro("O rapto do garoto de ouro");
        livro3.addAutor(new Autor("Marcos Rey"));
        livros.add(livro3);

        Tela tela = new Tela(new LivroTreeModel(livros));
        tela.setVisible(true);
    }
}
