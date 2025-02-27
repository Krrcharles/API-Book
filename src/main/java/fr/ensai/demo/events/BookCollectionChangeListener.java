package fr.ensai.demo.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import fr.ensai.demo.dto.BookCollectionDto;
import fr.ensai.demo.dto.BookDto;
import fr.ensai.demo.service.BookService;

@Component
public class BookCollectionChangeListener {

    @Autowired
    private BookService bookService;

    private double computeDistanceJaro(BookDto book1, BookDto book2) {
        JaroWinklerDistance jaro = new JaroWinklerDistance();
        Double titleSim = jaro.apply(book1.getTitle(), book2.getTitle());
        Double authorSim = jaro.apply(book1.getAuthorName(), book2.getAuthorName());
        return (titleSim + authorSim) / 2d;
    }

    private double computeDistanceJaccard(BookDto book1, BookDto book2) {
        JaccardSimilarity jaccard = new JaccardSimilarity();
        // jaccard.apply returns a similarity value (usually as an integer percentage)
        Double titleSim = jaccard.apply(book1.getTitle(), book2.getTitle());
        Double authorSim = jaccard.apply(book1.getAuthorName(), book2.getAuthorName());
        // Convert percentage to a fraction: divide by 100, then average.
        return (titleSim / 100d + authorSim / 100d) / 2d;
    }

    private void updateDistanceOnAdd(BookCollectionDto collection, BookDto addedBook) {
        List<Long> bookIdsBeforeAdd = new ArrayList<>(collection.getBookIds());
        bookIdsBeforeAdd.remove(addedBook.getBookId());
        int oldSize = bookIdsBeforeAdd.size();
        if (oldSize == 0) {
            collection.setDistanceJaro(0d);
            collection.setDistanceJaccard(0d);
            return;
        }
        int newSize = oldSize + 1;
        int oldPairCount = oldSize * (oldSize - 1) / 2;
        int newPairCount = newSize * (newSize - 1) / 2;
        double contributionToTotalJaro = 0d;
        double contributionToTotalJaccard = 0d;
        for (Long otherBookId : bookIdsBeforeAdd) {
            BookDto otherBook = bookService.getBookById(otherBookId);
            contributionToTotalJaro += computeDistanceJaro(addedBook, otherBook);
            contributionToTotalJaccard += computeDistanceJaccard(addedBook, otherBook);
        }
        double oldTotalJaro = collection.getDistanceJaro() * oldPairCount;
        double oldTotalJaccard = collection.getDistanceJaccard() * oldPairCount;
        double newTotalJaro = oldTotalJaro + contributionToTotalJaro;
        double newTotalJaccard = oldTotalJaccard + contributionToTotalJaccard;
        double newAvgJaro = newTotalJaro / newPairCount;
        double newAvgJaccard = newTotalJaccard / newPairCount;
        collection.setDistanceJaro(newAvgJaro);
        collection.setDistanceJaccard(newAvgJaccard);
    }

    private void updateDistanceOnRemove(BookCollectionDto collection, BookDto removedBook) {
        List<Long> bookIdsAfterRemove = new ArrayList<>(collection.getBookIds());
        bookIdsAfterRemove.remove(removedBook.getBookId());
        int newSize = bookIdsAfterRemove.size();
        if (newSize == 0) {
            collection.setDistanceJaro(0d);
            collection.setDistanceJaccard(0d);
            return;
        }
        int oldSize = newSize + 1;
        int oldPairCount = oldSize * (oldSize - 1) / 2;
        int newPairCount = newSize * (newSize - 1) / 2;
        double removedContributionJaro = 0d;
        double removedContributionJaccard = 0d;
        for (Long otherBookId : bookIdsAfterRemove) {
            BookDto otherBook = bookService.getBookById(otherBookId);
            removedContributionJaro += computeDistanceJaro(removedBook, otherBook);
            removedContributionJaccard += computeDistanceJaccard(removedBook, otherBook);
        }
        double oldTotalJaro = collection.getDistanceJaro() * oldPairCount;
        double oldTotalJaccard = collection.getDistanceJaccard() * oldPairCount;
        double newTotalJaro = oldTotalJaro - removedContributionJaro;
        double newTotalJaccard = oldTotalJaccard - removedContributionJaccard;
        double newAvgJaro = newTotalJaro / newPairCount;
        double newAvgJaccard = newTotalJaccard / newPairCount;
        collection.setDistanceJaro(newAvgJaro);
        collection.setDistanceJaccard(newAvgJaccard);
    }

    // Event listeners simply call the appropriate methods.
    @EventListener
    public void handleBookAdded(BookAddedToCollectionEvent event) {
        updateDistanceOnAdd(event.getCollection(), event.getBook());
    }

    @EventListener
    public void handleBookRemoved(BookRemovedFromCollectionEvent event) {
        updateDistanceOnRemove(event.getCollection(), event.getBook());
    }

}
