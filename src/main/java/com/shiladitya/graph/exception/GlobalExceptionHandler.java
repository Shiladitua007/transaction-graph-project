package com.shiladitya.graph.exception;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(NodeNotFoundException.class)
 public ResponseEntity < Map < String, String >> handleNodeNotFound(NodeNotFoundException ex) {
  Map < String, String > m = new HashMap < > ();
  m.put("error", "NODE_NOT_FOUND");
  m.put("message", ex.getMessage());
  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
 }

 @ExceptionHandler(InvalidDepthException.class)
 public ResponseEntity<Map < String, String >> handleInvalidDepth(InvalidDepthException ex) {
  Map < String, String > m = new HashMap < > ();
  m.put("error", "Invalid Max Depth input");
  m.put("message", ex.getMessage());
  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
 }

 @ExceptionHandler(CyclicException.class)
 public ResponseEntity<Map < String, String >> handleInvalidDepth(CyclicException ex) {
  Map < String, String > m = new HashMap < > ();
  m.put("error", "Cyclic Exception");
  m.put("message", ex.getMessage());
  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
 }

}