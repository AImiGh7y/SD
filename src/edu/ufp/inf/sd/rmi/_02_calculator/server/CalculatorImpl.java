package edu.ufp.inf.sd.rmi._02_calculator.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculatorImpl extends UnicastRemoteObject implements CalculatorRI {

    public CalculatorImpl() throws RemoteException {
        super();
    }

    @Override
    public double add(double a, double b) throws RemoteArithmeticException{
        double soma = a + b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "soma = {0}", soma);
        return soma;
    }

    @Override
    public double add(ArrayList<Double> list) throws RemoteException {
        double soma = 0;
        for (double a: list) {
            soma += a;
        }
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "soma = {0}", soma);
        return soma;
    }

    @Override
    public double div(double a, double b) throws RemoteException {
        if (b == 0) {
            throw new RemoteArithmeticException();
        }
        double div = a / b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "div = {0}", div);
        return div;
    }
}
