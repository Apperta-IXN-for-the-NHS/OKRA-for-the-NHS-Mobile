package com.emis.emismobile.cases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.emis.emismobile.cases.rest.CaseRestRepository;

import java.util.List;

public class CasesViewModel extends ViewModel {

    private final CaseRestRepository casesRestRepository = CaseRestRepository.getInstance();
    private LiveData<Case> selectedCase;
    private LiveData<List<Case>> allCases;

    public LiveData<Case> getCaseById(String caseId) {
        if (newCaseSelected(caseId)) {
            selectedCase = casesRestRepository.fetchCaseById(caseId);
        }

        return selectedCase;
    }

    private boolean newCaseSelected(String caseId) {
        return selectedCase == null
                || selectedCase.getValue() == null
                || !selectedCase.getValue().getId().equals(caseId);
    }

    public LiveData<List<Case>> getCases(String query, int limit, int start) {
        return casesRestRepository.fetchCases(query, limit, start);
    }
}