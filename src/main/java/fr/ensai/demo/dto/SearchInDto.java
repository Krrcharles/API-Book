package fr.ensai.demo.dto;

public class SearchInDto {
    public String search;
    public boolean caseSensitive;

    public String getSearch() {
        return search;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }
}
