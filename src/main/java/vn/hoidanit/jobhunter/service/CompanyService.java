package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompany(Company company) {
        Optional<Company> oldCompany = companyRepository.findById(company.getId());
        if (oldCompany.isPresent()) {
            oldCompany.get().setAddress(company.getAddress());
            oldCompany.get().setName(company.getName());
            oldCompany.get().setDescription(company.getDescription());
            oldCompany.get().setLogo(company.getLogo());
            return companyRepository.save(oldCompany.get());
        }

        return null;

    }

    public void deleteCompany(Long id) {
        Optional<Company> oldCompany = companyRepository.findById(id);
        if (oldCompany.isPresent()) {
            companyRepository.deleteById(id);
        }
    }

}
