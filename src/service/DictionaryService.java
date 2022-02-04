package service;

import exception.DictionaryFormatException;
import exception.EmptyDictionaryException;
import exception.ExistWordDictionaryException;
import exception.NotFoundWordDictionaryException;
import model.DictionaryModel;
import model.Phrase;

import java.io.IOException;
import java.util.Map;

public class DictionaryService {

    private final FileService fileService;

    public DictionaryService(FileService fileService) {
        this.fileService = fileService;
    }

    public Map<String, String> addPhrase(Phrase phrase, DictionaryModel dictionaryModel) throws IOException, DictionaryFormatException, ExistWordDictionaryException {

        Map<String, String> dictionaryMap = getDictionary(dictionaryModel.getPathFile());
        if(!phrase.getWord().matches(dictionaryModel.getPattern())){
            throw new DictionaryFormatException("Correct dictionary format - " + dictionaryModel.getPattern());
        }
        if(dictionaryMap.size() != 0){
            for(Map.Entry<String, String> entry : dictionaryMap.entrySet()){
                if(phrase.getWord().equals(entry.getKey())){
                    throw new ExistWordDictionaryException("Already exit translate with word: " + phrase.getWord());
                }
            }
        }
        dictionaryMap.put(phrase.getWord(), phrase.getTranslate());
        return fileService.writeToFile(dictionaryMap, dictionaryModel.getPathFile());
    }

    public Map<String, String> deletePhrase(String word, DictionaryModel dictionaryModel) throws IOException, NotFoundWordDictionaryException, EmptyDictionaryException {
        Map<String, String> dictionaryMap = getDictionary(dictionaryModel.getPathFile());

        for (Map.Entry<String, String> entry : dictionaryMap.entrySet()){
            if(!word.equals(entry.getKey())){
                throw new NotFoundWordDictionaryException("Not found word: " + word);
            }

            if(dictionaryMap != null ){
                throw new EmptyDictionaryException("Can't delete from empty dictionary");
            }

        }
        dictionaryMap.remove(word);
        return dictionaryMap;
    }

    public Map<String, String> getDictionary(String pathFile) throws IOException {
        return fileService.readFromFile(pathFile);
    }

}