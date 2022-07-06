package br.com.devdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 06/07/2022.
 */
public class PageableResponse<T> extends PageImpl<T> {
    private boolean last;
    private boolean first;
    private int totalPage; //esses atributos pode ser passados pra dentro do construtor abaixo tbm se necessário

    public PageableResponse(@JsonProperty("content") List<T> content, //content retornado da requisição paginada
                            @JsonProperty("number") int number,       //number retornado da requisição paginada
                            @JsonProperty("size") int size,           //size retornado da requisição paginada
                            @JsonProperty("totalElements") Long totalElements, //totalElement retornado da requisição paginada
                            @JsonProperty("sort") Sort sort) {
        super(content, PageRequest.of(number, size, sort), totalElements);
    }

    public PageableResponse() { //criado somente para o Parse do Json utilizar
        super(new ArrayList<>());
    }

    @Override
    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
