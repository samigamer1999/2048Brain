package com.ann.ttfe;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class Panel extends JPanel implements KeyListener, Runnable{

    private List<Grid> grids = new ArrayList<>();
    private Thread th;
    private boolean running = true;
    private int slider = 1;
    private int current_grid_index = 0;
    private List<Grid> saved_grids = new ArrayList<>();
    private int largest_score = 0, generations = 1, g_m_score = 0;
    private GA ga = new GA();
    private boolean playmode = false;
    private Grid grid = new Grid();
    private int largest_tile = 0 ;
    public Panel(){

        this.setPreferredSize(new Dimension(532,572));
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);
        //Initializing population of grids
        for(int i = 0; i < 250 ; i++){
            grids.add(new Grid());
        }
        //Starting
        th = new Thread(this);
        th.start();


    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //Update Screen info
        g2.setColor(Color.WHITE);
        g2.fillRect(0,532,532,5);
        g2.drawString("Training Mode", 30 , 552);
        g2.drawString("Gen: "+generations+"  Specie: "+current_grid_index+"  G_max_score: "+g_m_score+
                "  Max_score: "+largest_score+"  Largest_tile: "+largest_tile, 30, 565);
        g.setFont(new Font("ariel", Font.BOLD, 44));
        if(!grids.isEmpty() && !playmode){
            grids.get(current_grid_index).render(g2);
        }else{
            grid.render(g2);
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!playmode){


        if(e.getKeyCode() == KeyEvent.VK_UP ){
            if(slider < 500)slider++;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN && slider > 1 ){
            slider--;
        }
        }else{
            if(e.getKeyCode() == KeyEvent.VK_UP ){
                grid.move(grid.grid, "up");
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN  ){
                grid.move(grid.grid, "down");
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT ){
                grid.move(grid.grid, "left");
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT  ){
                grid.move(grid.grid, "right");
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void update() {
        if(playmode){

        }else{
            if(!grids.get(current_grid_index).getDone()){
                if(!grids.isEmpty())
                    grids.get(current_grid_index).play();
            }else{
                saved_grids.add(grids.get(current_grid_index));
                System.out.println("Grid : "+current_grid_index+"  ,Score: "+grids.get(current_grid_index).getScore());
                if(saved_grids.size() < 250){
                    current_grid_index++;
                }else{
                    //New generation
                    current_grid_index = 0;
                    generations++;
                    grids.clear();
                    g_m_score = 0;
                    grids = ga.nextGen(saved_grids);
                    saved_grids.clear();
                }

            }
            if(grids.get(current_grid_index).getScore() > largest_score){
                largest_score = grids.get(current_grid_index).getScore();
            }

            if(grids.get(current_grid_index).getScore() > g_m_score){
                g_m_score = grids.get(current_grid_index).getScore();
            }

            if(grids.get(current_grid_index).getLargest_tile() > largest_tile)
                    largest_tile = grids.get(current_grid_index).getLargest_tile();
        }

        if(largest_tile == 131072){
            System.out.println("Win,  gen: "+generations);
            running = false;
        }
    }

    @Override
    public void run() {
        while (running){
            try{
                //badly built but functional
                Thread.sleep(10);
            }catch(Exception e){
                e.printStackTrace();
            }
            repaint();
            for(int i = 0; i < slider ; i++){
                update();
            }
        }
    }


}
