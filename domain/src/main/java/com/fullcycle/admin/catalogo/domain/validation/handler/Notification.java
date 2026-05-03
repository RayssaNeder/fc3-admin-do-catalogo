package com.fullcycle.admin.catalogo.domain.validation.handler;

import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> erros;

    private Notification(final List<Error> erros){
        this.erros = erros;
    }

    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError){
        return new Notification(new ArrayList<>()).append(anError);
    }

    public static Notification create(final Throwable t){
        return create(new Error(t.getMessage()));
    }

    @Override
    public Notification append(Error anError) {
         this.erros.add(anError);
         return this;
    }

    @Override
    public Notification append(ValidationHandler anHandler) {
       this.erros.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public Notification validate(Validation aValidation) {
       try{
           aValidation.validate();
       }catch(final DomainException ex){
            this.erros.addAll(ex.getErrors());
       }catch(final Throwable t){
            this.erros.add(new Error(t.getMessage()));
       }
       return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.erros;
    }

    @Override
    public boolean hasError() {
        return ValidationHandler.super.hasError();
    }
}
