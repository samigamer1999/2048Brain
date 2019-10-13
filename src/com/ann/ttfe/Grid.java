package com.ann.ttfe;



import java.awt.*;
import java.util.*;
import java.util.List;


public class Grid {

     int[][] grid = new int[4][4];
    private int cte = 128;
    private Map<Integer, Color> clr = new HashMap<>();
    private Map<Integer, String> mvs = new HashMap<>();
    private NeuralNetwork brain;
    private boolean done = false;
    private float fitness = 0;
    private int score = 0;
    private int largest_tile = 0;
    private float empty_tiles_fit = 0;
    private float empty_tiles = 0;

    public Grid(){
        //Colors
        clr.put(2, Color.decode("#EEE4DA"));
        clr.put(4, Color.decode("#EDE0C8"));
        clr.put(8, Color.decode("#F2B179"));
        clr.put(16, Color.decode("#F59563"));
        clr.put(32, Color.decode("#F67C5F"));
        clr.put(64, Color.decode("#F65E3B"));
        clr.put(128, Color.decode("#EED06B"));
        clr.put(256, Color.decode("#EECD58"));
        clr.put(512, Color.decode("#EEC943"));
        clr.put(1024, Color.decode("#EEC62C"));
        clr.put(2048, Color.decode("#EEC308"));

        //Moves
        mvs.put(0, "up");
        mvs.put(1, "down");
        mvs.put(2, "right");
        mvs.put(3, "left");
        //initializing grid
        grid[new Random().nextInt(4)][new Random().nextInt(4)] = 2;
        int x = new Random().nextInt(4);
        int y = new Random().nextInt(4);
        while (grid[x][y] == 2){
             x = new Random().nextInt(4);
             y = new Random().nextInt(4);
        }
        grid[x][y] = 2;

        //starting brain
        brain = new NeuralNetwork(16, 16, 4);

    }


    public Grid(NeuralNetwork brain){
        //Colors
        clr.put(2, Color.decode("#EEE4DA"));
        clr.put(4, Color.decode("#EDE0C8"));
        clr.put(8, Color.decode("#F2B179"));
        clr.put(16, Color.decode("#F59563"));
        clr.put(32, Color.decode("#F67C5F"));
        clr.put(64, Color.decode("#F65E3B"));
        clr.put(128, Color.decode("#EED06B"));
        clr.put(256, Color.decode("#EECD58"));
        clr.put(512, Color.decode("#EEC943"));
        clr.put(1024, Color.decode("#EEC62C"));
        clr.put(2048, Color.decode("#EEC308"));
        //Moves
        mvs.put(0, "up");
        mvs.put(1, "down");
        mvs.put(2, "right");
        mvs.put(3, "left");
        //initializing grid
        grid[new Random().nextInt(4)][new Random().nextInt(4)] = 2;
        int x = new Random().nextInt(4);
        int y = new Random().nextInt(4);
        while (grid[x][y] == 2){
            x = new Random().nextInt(4);
            y = new Random().nextInt(4);
        }
        grid[x][y] = 2;

        //starting brain
        this.brain = brain;
    }

    public void render(Graphics2D g){
        g.setColor(Color.decode("#BBADA0"));
        render_grid_layout(g);
        FontMetrics fm = g.getFontMetrics();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(grid[i][j] == 0){
                    g.setColor(Color.decode("#CDC1B4"));
                    g.fillRect(j*(cte + 4) + 4,i*(cte +4) + 4 , cte, cte);
                }else{
                    g.setColor(clr.get(grid[i][j]));
                    g.fillRect(j*(cte + 4) + 4,i*(cte +4) + 4 , cte, cte);
                    int stringWidth = fm.stringWidth( Integer.toString(grid[i][j]) );
                    int startX = j*(cte + 4) + 4 + ( ( cte - stringWidth ) / 2 );
                    int startY = i*(cte + 4) - 8  + ( ( cte + fm.getHeight() ) / 2 );
                    g.setColor(Color.decode("#776E65"));
                    g.drawString(Integer.toString(grid[i][j]), startX, startY);
                }
            }
        }
    }

    public void render_grid_layout(Graphics2D g){
        g.fillRect(0,0, 4, 528);
        g.fillRect(132,0, 4, 528);
        g.fillRect(264,0, 4, 528);
        g.fillRect(396,0, 4, 528);
        g.fillRect(528,0, 4, 528);
        g.fillRect(0,0, 528, 4);
        g.fillRect(0,132,  528, 4);
        g.fillRect(0,264, 528, 4);
        g.fillRect(0,396, 528, 4);
        g.fillRect(0,528, 532, 4);
    }

    public void move(int[][] grid, String way){

        switch(way){
            case "right":
                for(int i = 0; i < 4; i++){
                    List<Integer> temp = new ArrayList<>();
                    for(int j = 3; j >= 0; j--){
                        if(grid[i][j] != 0){
                            temp.add(grid[i][j]);
                            grid[i][j] = 0;
                        }
                    }
                    shrink(temp);
                    for(int j = 0; j < temp.size(); j++){
                        grid[i][3 - j] = temp.get(j);
                    }
                }
                break;
            case "left":
                for(int i = 0; i < 4; i++){
                    List<Integer> temp = new ArrayList<>();
                    for(int j = 0; j < 4; j++){
                        if(grid[i][j] != 0){
                            temp.add(grid[i][j]);
                            grid[i][j] = 0;
                        }
                    }
                    shrink(temp);
                    for(int j = 0; j < temp.size(); j++){
                        grid[i][j] = temp.get(j);
                    }
                }
                break;
            case "down":
                for(int j = 0; j < 4; j++){
                    List<Integer> temp = new ArrayList<>();
                    for(int i = 3; i >= 0; i--){
                        if(grid[i][j] != 0){
                            temp.add(grid[i][j]);
                            grid[i][j] = 0;
                        }
                    }
                    shrink(temp);
                    for(int i = 0; i < temp.size(); i++){
                        grid[3 - i][j] = temp.get(i);
                    }
                }
                break;
            case "up":
                for(int j = 0; j < 4; j++){
                    List<Integer> temp = new ArrayList<>();
                    for(int i = 0; i < 4; i++){
                        if(grid[i][j] != 0){
                            temp.add(grid[i][j]);
                            grid[i][j] = 0;
                        }
                    }
                    shrink(temp);
                    for(int i = 0; i < temp.size(); i++){
                        grid[i][j] = temp.get(i);
                    }
                }
                break;
        }


    }

    private void add_rand_tile(int[][] grid) {
        int x = new Random().nextInt(4);
        int y = new Random().nextInt(4);
        while (grid[x][y] != 0){
            x = new Random().nextInt(4);
            y = new Random().nextInt(4);
        }
        grid[x][y] = 2;
    }

    private void shrink(List<Integer> temp) {
        for (int i = 0; i < temp.size()-1 ; i++){
            if(temp.get(i).equals(temp.get(i+1))){
                temp.set(i, temp.get(i) * 2);
                score += temp.get(i) * 2;
                if(largest_tile < temp.get(i) * 2){
                    largest_tile = temp.get(i) * 2;
                }
                temp.set(i + 1, 0);
            }
        }
        temp.removeAll(Collections.singleton(0));
    }

    public void play() {
        empty_tiles = 0;
        //copy of grid (check illegal move)
        int[][] copy =  new int[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                copy[i][j] = grid[i][j];
                if(grid[i][j] == 0){
                    empty_tiles += 1;
                }
            }
        }

        empty_tiles_fit += empty_tiles / 16;

        float[] inputs = new float[16];
        int sum = 0;
        //inputs
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 4 ;i++){
            for(int j = 0; j < 4; j++){
                list.add(grid[i][j]);
                sum += grid[i][j];
            }
        }
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = list.get(i)/sum;
        }
        float[] output =  brain.feedforward(inputs);
        int max = max(output);
        this.move(grid, mvs.get(max));

        //illegal move check
        boolean mouvement = false;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(copy[i][j] != grid[i][j]){
                    mouvement = true;
                    break;
                }
            }
            if (mouvement)break;
        }

        if (!mouvement){
            List<Integer> Pos = Possibilities();
            if(!Pos.isEmpty()){
                move(grid, mvs.get(Pos.get(new Random().nextInt(Pos.size()))));
            }else{
                done = true;
            }
        }

           if(!isFull()){
               add_rand_tile(grid);
           }


    }

    private Integer max(float[] output) {
        int index = 0;
        float max_value = output[0];
        for(int i = 1; i < 4; i++){
            if(output[i] > max_value){
                max_value = output[i];
                index = i;
            }
        }
        return index;
    }

    private boolean isFull(){
        boolean full = true;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(grid[i][j] == 0){
                    full = false;
                    break;
                }
            }
            if(!full)break;
        }
        return full;
    }

    private List<Integer> Possibilities(){
        List<Integer> pos = new ArrayList<>();
        boolean done = false;
        for(int x = 0; x < 4 ; x++){
            int[][] copy =  new int[4][4];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    copy[i][j] = grid[i][j];
                }
            }
            this.move(copy, mvs.get(x));
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(copy[i][j] != grid[i][j]){
                        pos.add(x);
                        done = true;
                        break;
                    }
                }
                if (done)break;
            }

        }
        return pos;
    }

    public boolean getDone() {
        return done;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public Grid child(){
        return new Grid(new NeuralNetwork(this.brain, this::mutate));
    }

    public float mutate(float x){

        if(new Random().nextFloat() < 0.1){
            return x + new Random().nextFloat() - 0.5f ;
        }else{
            return x;
        }
    }
    public void printInfo() {
        this.brain.weights_ih.printMatrix();
        this.brain.weights_ho.printMatrix();
        this.brain.bias_h.printMatrix();
        this.brain.bias_o.printMatrix();
    }

    public int getLargest_tile(){
        return largest_tile;
    }

    public float getEmpty_tiles_fit() {
        return empty_tiles_fit;
    }

    public void setEmpty_tiles_fit(float empty_tiles_fit) {
        this.empty_tiles_fit = empty_tiles_fit;
    }
}
