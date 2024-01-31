package cg.tcarespb.repository;

import cg.tcarespb.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate,String> {
    @Query( value =
            "SELECT r.id " +
                    "FROM rates r " +
                    "WHERE r.star_quantity >= 4 " +
                    "ORDER BY r.rate_quantity DESC " +
                    "LIMIT 3; ",nativeQuery = true)
    List<String> findTop3EmployeesWithHighestRate();
}
