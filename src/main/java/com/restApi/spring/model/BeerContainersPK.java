package com.restApi.spring.model;

import java.io.Serializable;

/**
 * Created by felipe on 25/8/18.
 */
public class BeerContainersPK implements Serializable {
    private Beer beer;
    private Containers containers;

    @Override
    public int hashCode(){
        return this.beer.hashCode() + this.containers.hashCode();
    }

    @Override
    public boolean equals(Object o){
        Boolean flag = false;
        BeerContainersPK myId = (BeerContainersPK)o;

        if ((o instanceof BeerContainersPK)
                && (this.beer.equals(myId.beer))
                && (this.containers.equals(myId.containers))){
            flag = true;
        }

        return flag;
    }
}
