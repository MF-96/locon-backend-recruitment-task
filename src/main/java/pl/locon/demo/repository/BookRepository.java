package pl.locon.demo.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import pl.locon.demo.model.*;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
}
