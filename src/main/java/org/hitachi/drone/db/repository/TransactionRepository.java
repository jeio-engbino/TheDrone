package org.hitachi.drone.db.repository;

import org.hitachi.drone.db.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByDroneId(Long droneId);

}
