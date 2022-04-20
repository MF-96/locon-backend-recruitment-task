package pl.locon.demo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Book {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "AUTHOR")
  private String author;
}
