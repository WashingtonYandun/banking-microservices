package com.tcs.cliente_service.util;

public interface Either<L, R> {
    boolean isRight();
    boolean isLeft();
    L getLeft();
    R getRight();

    static <L, R> Either<L, R> left(L value) { return new Left<>(value); }
    static <L, R> Either<L, R> right(R value) { return new Right<>(value); }
}

class Left<L, R> implements Either<L, R> {
    private final L value;
    Left(L value) { this.value = value; }
    public boolean isRight() { return false; }
    public boolean isLeft() { return true; }
    public L getLeft() { return value; }
    public R getRight() { throw new IllegalStateException("No Right value"); }
}

class Right<L, R> implements Either<L, R> {
    private final R value;
    Right(R value) { this.value = value; }
    public boolean isRight() { return true; }
    public boolean isLeft() { return false; }
    public L getLeft() { throw new IllegalStateException("No Left value"); }
    public R getRight() { return value; }
}