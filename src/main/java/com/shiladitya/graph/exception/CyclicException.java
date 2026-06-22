package com.shiladitya.graph.exception;

public class CyclicException extends RuntimeException{
 public CyclicException(String id){super("Graph node "+id+" does not exist");}
}
