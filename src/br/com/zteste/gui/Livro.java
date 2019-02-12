/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.zteste.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class Livro {
    private String nome;
    private List<Autor> autores = new ArrayList();
    
    public Livro(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    @Override
    public String toString(){
        return getNome();
    }
    
    public void addAutor(Autor autor){
        autores.add(autor);
    }
    
    public List getAutores(){
        return Collections.unmodifiableList(autores);
    }
    
}
