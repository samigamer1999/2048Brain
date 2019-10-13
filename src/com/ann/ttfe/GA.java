package com.ann.ttfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {

    public GA(){

    }

    public List<Grid> nextGen(List<Grid> saved_grids){
        calculateFitness(saved_grids);
        return generate(saved_grids);
    }

    private List<Grid> generate(List<Grid> saved_grids) {
        List<Grid> new_grids = new ArrayList<>();
        for(int i = 0  ; i < 175; i++){
            // pick grid based on fitness
            // the selection will eventually be mutated
            new_grids.add(selection(saved_grids));
        }
        for(int i = 0 ; i < 75 ; i++){
            new_grids.add(new Grid());
        }
        return new_grids;
    }

    private Grid selection(List<Grid> saved_grids) {
        // Start at 0
        int index = 0;

        // Pick a random number between 0 and 1
        float r = new Random().nextFloat();
        do{
            r -= saved_grids.get(index).getFitness();
            index ++;
        }while(r > 0);

        index--;

        return saved_grids.get(index).child();
    }

    public void calculateFitness(List<Grid> saved_grids){
        float sum = 0;
        float sum_tiles = 0;
        for(Grid grid : saved_grids){
            sum += grid.getScore();
            sum_tiles += grid.getEmpty_tiles_fit();
        }

        for(Grid grid : saved_grids){
            grid.setFitness((float)grid.getScore()/(2*sum) + grid.getEmpty_tiles_fit()/(2*sum_tiles));
        }
    }
}
