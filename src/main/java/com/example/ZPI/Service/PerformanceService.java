package com.example.ZPI.Service;

import com.example.ZPI.Repository.PerformanceRepository;
import com.example.ZPI.entities.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    CounterService counterService;

    public Performance addPerformance(Performance performance) {
        performance.setPerformanceId(counterService.getNextId("performance"));
        performanceRepository.save(performance);
        return performance;
    }
}
