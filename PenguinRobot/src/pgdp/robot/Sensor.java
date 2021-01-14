package pgdp.robot;

import java.util.function.Consumer;

public abstract class Sensor<T> {
    protected Robot owner;
    protected Consumer<T> processor = new Consumer<T>() {
        @Override
        public void accept(T t) {
            //TODO this accepts a data T from a sensor, but what should it do with it?.
            //maybe because in real usage the processor gets set new anyway, it doesnt matter?
        }
    };

    //Setter
    public void setOwner(Robot owner) {
        this.owner = owner;
    }

    public Sensor<T> setProcessor(Consumer<T> processor) {
        this.processor = processor;
        return this;
    }

    public abstract T getData();

}