package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
@Data
@Builder
@JsonPropertyOrder(value = {"id", "amount", "issueDate", "member", "librarian"})
public class Fine implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonIgnore
    private Librarian librarian; // Librarian processing this fine

    @ManyToOne
    @JoinColumn(name = "admin_id") // Foreign key to Admin
    @JsonIgnore
    private Admin admin; // Admin managing this fine

    /**
     * Calculates the fine amount based on the due date and current date.
     * The fine grows per day the book is overdue.
     *
     * @param dueDate     The due date of the book.
     * @param currentDate The current date for calculating the overdue days.
     * @return The calculated fine amount.
     */
    public static double calculateFineAmount(Date dueDate, Date currentDate) {
        if (null == dueDate || currentDate.before(dueDate) || currentDate.equals(dueDate)) {
            return 0.0;
        }

        long daysOverdue = (currentDate.getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24);

        double baseFine = 2.0; // Base fine amount in euros
        double incrementPerDay = 1.0; // Additional fine per day in euros

        // Calculate the total fine based on the number of overdue days
        double totalFine = baseFine + (daysOverdue - 1) * incrementPerDay; // Initial base fine plus incremental fine

        return totalFine > 0 ? totalFine : 0; // Ensure that fine is not negative
    }

}