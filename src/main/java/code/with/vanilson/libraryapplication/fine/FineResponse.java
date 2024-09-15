package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/**
 * FineResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineResponse extends RepresentationModel<FineResponse> {
    private Long id;
    private double amount;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Boolean isPaid;
    private MemberResponse memberId;
    private LibrarianResponse librarianId;
    private AdminResponse adminId;
}