package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Fine
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@SuppressWarnings("unused")
@Entity(name = "Fine")
@Table(name = "fines")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonPropertyOrder(value = {"id", "amount", "issueDate", "dueDate", "isPaid", "member", "librarian", "admin"})
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "librarian_id", nullable = false)
    private Librarian librarian;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public Fine(double amount, LocalDate issueDate, LocalDate dueDate, Boolean isPaid, Member member,
                Librarian librarian,
                Admin admin) {
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
        this.member = member;
        this.librarian = librarian;
        this.admin = admin;
    }
}