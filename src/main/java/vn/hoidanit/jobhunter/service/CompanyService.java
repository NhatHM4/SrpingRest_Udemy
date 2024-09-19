package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setData(pCompany.getContent());
        return rs;
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
