package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ResultPaginationDTO getAllCompanies(Optional<String> currentOptional, Optional<String> pageSizeOptional) {
        Pageable paging = null;
        Page<Company> pagedResult = null;
        ResultPaginationDTO rs = new ResultPaginationDTO();
        if (currentOptional.isPresent() && pageSizeOptional.isPresent()) {
            paging = PageRequest.of(Integer.parseInt(currentOptional.get()) - 1,
                    Integer.parseInt(pageSizeOptional.get()));
            pagedResult = companyRepository.findAll(paging);
        }
        if (pagedResult != null && pagedResult.hasContent()) {
            Meta meta = new Meta();
            meta.setPage(pagedResult.getNumber() + 1);
            meta.setPageSize(pagedResult.getSize());
            meta.setPages(pagedResult.getTotalPages());
            meta.setTotal(pagedResult.getTotalElements());
            rs.setMeta(meta);
            rs.setData(pagedResult.getContent());
            return rs;
        }
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
