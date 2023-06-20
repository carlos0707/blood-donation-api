package br.com.carlos.blooddonationapi.routes;

import br.com.carlos.blooddonationapi.entities.Response;
import br.com.carlos.blooddonationapi.repository.DonorRepository;
import br.com.carlos.blooddonationapi.services.BloodDonationService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/blood-donation")
public class BloodDonation {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DonorRepository repository;

    @PostMapping(value="/extraction-data", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public @ResponseBody ResponseEntity<?> get(
            @RequestParam("file") MultipartFile file
            ) {

        Response response             = new Response();
        BloodDonationService service  = new BloodDonationService(repository);

        try {

            if(file.isEmpty()) throw new Exception("File doesn't exists");

            service.persistFile(file);

            response.setSuccess(true);
            response.setResult(Arrays.asList(service.extractAllDataFromFile(file)));

            if(!service.findDonor(file)) {

                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("startAt", System.currentTimeMillis()).toJobParameters();
                jobLauncher.run(job, jobParameters);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/all-donors", produces = "application/json")
    public @ResponseBody ResponseEntity<?> getDonors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        BloodDonationService service  = new BloodDonationService(repository);

        try {
            return new ResponseEntity<>(service.findAllDonors(page, size), HttpStatus.OK);
        }catch (Exception e) {

            e.printStackTrace();

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }

}
