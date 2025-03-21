package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.member.Member;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * FineManagementService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-03-21
 */
@Component
public class FineManagementService {

    private static final int GRACE_PERIOD_DAYS = 3; // Exemplo de período de carência
    private static final double FINE_RATE_PER_DAY = 1.0; // Exemplo de taxa de multa por dia
    private static final double MAX_FINE = 50.0; // Exemplo de multa máxima

    /**
     * Calculates the fine for a book overdue.
     *
     * @param issueDate The date the book was issued.
     * @param dueDate   The due date for returning the book.
     * @param amount    The initial amount for the fine if not overdue.
     * @param member    The member who borrowed the book.
     * @param librarian The librarian who handled the fine.
     * @param admin     The admin who approved the fine.
     * @param isPaid    Indicates if the fine has been paid.
     * @return A Fine entity representing the calculated fine.
     */
    public Fine calculateFine(LocalDate issueDate, LocalDate dueDate, double amount, Member member,
                              Librarian librarian, Admin admin, Boolean isPaid) {
        Fine fine = new Fine();
        fine.setIssueDate(issueDate);
        fine.setDueDate(dueDate);
        fine.setMember(member);
        fine.setLibrarian(librarian);
        fine.setAdmin(admin);
        fine.setIsPaid(isPaid);

        if (LocalDate.now().isAfter(dueDate)) {
            long daysOverdue = calculateDaysOverdue(dueDate);
            long chargeableDays = Math.max(0, daysOverdue - GRACE_PERIOD_DAYS);
            double fineAmount = Math.min(chargeableDays * FINE_RATE_PER_DAY, MAX_FINE);
            fine.setAmount(fineAmount);
        } else {
            fine.setAmount(amount); // Use the amount from the FineRequest if not overdue
        }

        return fine;
    }

    /**
     * Calculates the number of days overdue for a book based on the due date.
     *
     * @param dueDate The due date for returning the book.
     * @return The number of days overdue. If the due date is in the future, returns 0.
     */
    private long calculateDaysOverdue(LocalDate dueDate) {
        return Math.max(0, ChronoUnit.DAYS.between(dueDate, LocalDate.now()));
    }

    // Outros métodos para gerenciar multas podem ser adicionados aqui
    public void recordFine(Fine fine) {
        // Lógica para registrar a multa no sistema (por exemplo, salvar em um repositório)
    }

    public void updateFine(Fine fine) {
        // Lógica para atualizar uma multa existente
    }

    public List<Fine> getFinesForMember(Member member) {
        // Lógica para recuperar todas as multas de um membro específico
        return new ArrayList<>(); // Exemplo de retorno
    }

    // Outros métodos relacionados à gestão de multas podem ser implementados
}
