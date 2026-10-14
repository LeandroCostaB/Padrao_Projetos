package com.example.prototype;

/**
 * Contrato para objetos capazes de criar uma cópia de si mesmos.
 *
 * @param <T> tipo produzido pela clonagem
 */
public interface Prototype<T> {

    T clone();
}
