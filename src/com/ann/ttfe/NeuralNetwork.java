package com.ann.ttfe;

import java.util.function.Function;

public class NeuralNetwork {

    private int input_neurones , hidden_neurones, output_neurones;
    public Matrix weights_ih , weights_ho;
    public Matrix bias_h , bias_o;


    //Constructor for random neural network
    public NeuralNetwork(int inputs, int hidden, int outputs){
        this.input_neurones = inputs;
        this.hidden_neurones = hidden;
        this.output_neurones = outputs;

        //Initialising weight matrices randomly
        this.weights_ih = new Matrix(hidden_neurones, input_neurones);
        this.weights_ih.randomize();
        this.weights_ho = new Matrix(output_neurones, hidden_neurones);
        this.weights_ho.randomize();

        //Initialising bias vectors
        this.bias_h = new Matrix(hidden_neurones, 1);
        bias_h.randomize();
        this.bias_o = new Matrix(output_neurones, 1);
        bias_o.randomize();
    }

    //Constructor for mutated copy of a neural network
    public NeuralNetwork(NeuralNetwork neuralNetwork, Function<Float, Float> mutate){

        //Initialising weight matrices based on this particular NN
        this.weights_ih = neuralNetwork.weights_ih.copy();
        this.weights_ih.map(mutate);
        this.weights_ho = neuralNetwork.weights_ho.copy();
        this.weights_ho.map(mutate);

        //Initialising bias vectors based on this particular NN
        this.bias_h = neuralNetwork.bias_h.copy();
        this.bias_h.map(mutate);
        this.bias_o = neuralNetwork.bias_o.copy();
        this.bias_o.map(mutate);
    }

    //Constructor for pre-made weights and biases based neural network
    public NeuralNetwork(Matrix weights_ih, Matrix weights_ho, Matrix bias_h, Matrix bias_o){

        this.weights_ih = weights_ih;
        this.weights_ho = weights_ho;
        this.bias_h = bias_h;
        this.bias_o = bias_o;

    }

    public float[] feedforward(float[] input_array){
        //Calculate hidden vector
        Matrix inputs = Matrix.fromArray(input_array);
        Matrix hidden = weights_ih.multiply(inputs);
        hidden.add(bias_h);
        hidden.map(this::sigmoid);

        //Calculate output vector
        Matrix output = weights_ho.multiply(hidden);
        output.add(bias_o);
        output.map(this::sigmoid);

        return Matrix.toArray(output);
    }

    public float sigmoid(float x){
        return (float)(1/(1+Math.exp(-x)));
    }




}

