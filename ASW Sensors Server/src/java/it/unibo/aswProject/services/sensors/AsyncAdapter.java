package it.unibo.aswProject.services.sensors;



import javax.servlet.*;
import java.io.*;

public class AsyncAdapter implements AsyncListener{
    @Override
    public void onComplete(AsyncEvent event) throws IOException {}
    @Override
    public void onError(AsyncEvent event) throws IOException {}
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {}
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {}
}
