package com.fullcycle.admin.catalogo.domain.exceptions;

import java.util.List;
import com.fullcycle.admin.catalogo.domain.validation.Error;

public class DomainException extends NoStacktraceException{

    private final List<Error> erros;

    private DomainException(final String aMessage, final List<Error> anErros) {
        super(aMessage);
        this.erros = anErros;
    }

    public static DomainException with(final Error aError){
        return new DomainException(aError.message(),List.of(aError));
    }

    public static DomainException with(final List<Error> anErrors){
        return new DomainException("",anErrors);
    }

    public List<Error> getErrors() {
        return erros;
    }
}
