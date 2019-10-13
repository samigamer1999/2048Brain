package com.ann.ttfe;

import java.util.Random;
import java.util.function.Function;

public class Matrix {

    private float[][] matrix ;
    private int rows = 0, cols = 0;

    public Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        matrix = new float[rows][cols];
    }

    public Matrix(float[][] matrix){
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.matrix = matrix;
    }

    public void randomize(){
        for (int i = 0 ; i < rows; i++){
            for (int j = 0 ; j < cols; j++){
                matrix[i][j] = new Random().nextFloat()*2 -1;
            }
        }
    }

    public void printMatrix(){
        for (int i = 0 ; i < rows; i++){
            for (int j = 0 ; j < cols; j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int[] getDim(){
        return new int[]{rows, cols};
    }

    public float[][] getMatrix(){
        return matrix;
    }

    public void add(Object x){
        if(x instanceof Matrix){
            for (int i = 0 ; i < rows; i++){
                for (int j = 0 ; j < cols; j++){
                    matrix[i][j] += ((Matrix) x).getMatrix()[i][j];
                }
            }
        }else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] += (float) x;
                }
            }
        }
    }

    public void scale(float x){
        for (int i = 0 ; i < rows; i++){
            for (int j = 0 ; j < cols; j++){
                matrix[i][j] *= x;
            }
        }
    }

    public Matrix multiply(Matrix m){
        if(this.cols != m.getDim()[0]){
            System.out.println("Dimensions are mismatched");
            return null;
        }else{
            Matrix result = new Matrix(this.rows, m.getDim()[1]);
            for(int i = 0; i < result.getDim()[0]; i++){
                for(int j = 0; j < result.getDim()[1]; j++){
                    float sum = 0;
                    for(int k = 0; k < this.cols; k++){
                        sum += this.matrix[i][k]* m.getMatrix()[k][j];
                    }
                    result.matrix[i][j] = sum;
                }
            }
            return result;
        }
    }

    public Matrix transpose(){
        Matrix result = new Matrix(this.cols, this.rows);
        for (int i = 0 ; i < result.rows; i++){
            for (int j = 0 ; j < result.cols; j++){
                result.matrix[i][j] = this.matrix[j][i];
            }
        }
        return result;
    }

    public void map(Function<Float, Float> f){
        for (int i = 0 ; i < this.rows; i++){
            for (int j = 0 ; j < this.cols; j++){
                this.matrix[i][j] = f.apply(this.matrix[i][j]);
            }
        }
    }

    public static Matrix fromArray(float[] arr){
        Matrix result = new Matrix(arr.length, 1);
        for (int i = 0 ; i < result.rows; i++){
            result.matrix[i][0] = arr[i];
        }
        return result;
    }

    public static float[] toArray(Matrix vector){
        float[] result = new float[vector.getDim()[0]];
        for (int i = 0 ; i < result.length; i++){
            result[i] = vector.matrix[i][0];
        }
        return result;
    }

    public Matrix copy(){
        float[][] copy = new float[this.rows][this.cols];
        for (int i = 0 ; i < this.rows; i++){
            for (int j = 0 ; j < this.cols; j++){
                copy[i][j] = this.matrix[i][j];
            }
        }
        return new Matrix(copy);
    }
}
