package com.example.patientmvc.web;


import com.example.patientmvc.Repositories.PatientRepository;
import com.example.patientmvc.entities.Patient;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping(path = "/index")
    public String Patient(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "Name", defaultValue = "") String Name)
    {
        Page<Patient> patients= patientRepository.findByNameContains(Name, PageRequest.of(page, size));

        model.addAttribute("patients", patients.getContent());
        model.addAttribute("pages", new int[patients.getTotalPages()]);
        model.addAttribute("Name", Name);
        model.addAttribute("Name2", Name);
        model.addAttribute("currentpage", page);
        return "patient";
    }

    @GetMapping("/delete")
    public String Delete(Long Id, int page, String Name){
        patientRepository.deleteById(Id);
        return "redirect:/index?page="+ page+ "&Name="+Name;
    }

    @GetMapping(path = "/patients")
    @ResponseBody
    public List<Patient> listPatient(){
       return patientRepository.findAll();
    }

    @GetMapping(path = "/")
    public String home(){
        return "redirect:/index";
    }

    @GetMapping(path = "/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping(path = "/save")
    //une fois tu cree un patient tu fait la validation
    public String Save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String Name){
        if(bindingResult.hasErrors())  return "formPatients";

        patientRepository.save(patient);
        return "redirect:/index?page=" +page+ "&Name="+Name;
    }

    @GetMapping(path = "/edit")
    public String editPatient(Model model, Long id, int page, String Name){
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient introuvable");

        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("Name", Name);
        return "editPatients";
    }
}
