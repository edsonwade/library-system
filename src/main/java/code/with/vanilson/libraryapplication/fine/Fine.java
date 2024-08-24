package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Fine
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@Entity
@Table(name = "fines")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonPropertyOrder(value = {"id","amount","issueDate","member","librarian"})
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fine_id_seq")
    @SequenceGenerator(name = "fine_id_seq", sequenceName = "fine_id_seq", allocationSize = 1)
    @Column(name = "fine_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dueDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian; // Librarian processing this fine

}