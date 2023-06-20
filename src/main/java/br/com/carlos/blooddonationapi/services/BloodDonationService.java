package br.com.carlos.blooddonationapi.services;

import br.com.carlos.blooddonationapi.entities.Donor;
import br.com.carlos.blooddonationapi.entities.ExtractionData;
import br.com.carlos.blooddonationapi.entities.Response;
import br.com.carlos.blooddonationapi.helper.DateHelper;
import br.com.carlos.blooddonationapi.repository.DonorRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BloodDonationService {

    @Autowired
    DonorRepository repository;

    public BloodDonationService(DonorRepository repository) {
        this.repository = repository;
    }

    public BloodDonationService(){}

    public ExtractionData extractAllDataFromFile(MultipartFile file) throws IOException {

        String json = new String(file.getBytes(), StandardCharsets.UTF_8);

        List<Donor> documents = new ArrayList<>();

        this.toListOfDocuments(json).stream().forEach(s -> {
            documents.add(new Donor(s));
        });

        Document doc = new Document("candidatesByState", this.totalFilterByState(documents))
                .append("imcAgeGroup", this.avgImcByAgeGroup(documents))
                .append("percentObesity", this.percentageObesity(documents))
                .append("averageByBloodType", this.avgByBloodType(documents))
                .append("totalDonorsByBloodType", this.totalDonorByType(documents));

        return new ExtractionData(doc);
    }

    private List<Document> toListOfDocuments(String json){
        Document doc = Document.parse("{ \"list\":"+json+"}");
        Object list = doc.get("list");
        if(list instanceof List<?>) {
            return (List<Document>) doc.get("list");
        }
        return null ;
    }

    private Document totalDonorByType(List<Donor> documents) {

        Integer aMais  = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("A+") || s.getTipoSanguineo().equals("A-") || s.getTipoSanguineo().equals("O+") || s.getTipoSanguineo().equals("O-")).count());
        Integer aMenos  = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("A-")|| s.getTipoSanguineo().equals("O-")).count());
        Integer bMais   = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("B+") || s.getTipoSanguineo().equals("B-") || s.getTipoSanguineo().equals("O+") || s.getTipoSanguineo().equals("O-")).count());
        Integer bMenos  = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("B-") || s.getTipoSanguineo().equals("O-")).count());
        Integer abMais  = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("A+") || s.getTipoSanguineo().equals("A-") || s.getTipoSanguineo().equals("O+") || s.getTipoSanguineo().equals("O-") || s.getTipoSanguineo().equals("AB-") || s.getTipoSanguineo().equals("B-") || s.getTipoSanguineo().equals("B+") || s.getTipoSanguineo().equals("AB+")).count());
        Integer abMenos =Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("A-") || s.getTipoSanguineo().equals("B-") || s.getTipoSanguineo().equals("O-") || s.getTipoSanguineo().equals("AB-")).count());
        Integer oMais  =Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("O+") || s.getTipoSanguineo().equals("O-")).count());
        Integer oMenos  = Math.toIntExact(documents.stream().filter(s -> s.getIdade() >= 16 && s.getIdade() <= 69 && s.getPeso() > 50).filter(s -> s.getTipoSanguineo().equals("O-")).count());


        return new Document("aMais", aMais)
                .append("aMenos", aMenos)
                .append("bMais", bMais)
                .append("bMenos", bMenos)
                .append("abMais", abMais)
                .append("abMenos", abMenos)
                .append("oMais", oMais)
                .append("oMenos", oMenos);
    }

    private Document avgByBloodType(List<Donor> documents) {

        double avgAmais  = documents.stream().filter(s -> s.getTipoSanguineo().equals("A+")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgAmenos = documents.stream().filter(s -> s.getTipoSanguineo().equals("A-")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgBmais  = documents.stream().filter(s -> s.getTipoSanguineo().equals("B+")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgBmenos = documents.stream().filter(s -> s.getTipoSanguineo().equals("B-")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgABmais  = documents.stream().filter(s -> s.getTipoSanguineo().equals("AB+")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgABmenos = documents.stream().filter(s -> s.getTipoSanguineo().equals("AB-")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgOmais  = documents.stream().filter(s -> s.getTipoSanguineo().equals("O+")).mapToInt(s -> s.getIdade()).average().orElse(0);
        double avgOmenos = documents.stream().filter(s -> s.getTipoSanguineo().equals("O-")).mapToInt(s -> s.getIdade()).average().orElse(0);

        return new Document("aMais", avgAmais)
                .append("aMenos", avgAmenos)
                .append("bMais", avgBmais)
                .append("bMenos", avgBmenos)
                .append("abMais", avgABmais)
                .append("abMenos", avgABmenos)
                .append("oMais", avgOmais)
                .append("oMenos", avgOmenos);
    }

    private Document percentageObesity(List<Donor> documents) {

        Document document        = new Document();

        Integer totalMasc        = Math.toIntExact(documents.stream().filter(s -> s.getSexo().equals("Masculino")).count());
        Integer totalObesityMasc = Math.toIntExact(documents.stream().filter(s -> s.getSexo().equals("Masculino") && this.calcImc(s.getPeso(), s.getAltura()) > 30).count());

        document.put("percenteMasc", this.calcPercentage(totalObesityMasc, totalMasc));

        Integer totalFem        = Math.toIntExact(documents.stream().filter(s -> s.getSexo().equals("Feminino")).count());
        Integer totalObesityFem = Math.toIntExact(documents.stream().filter(s -> s.getSexo().equals("Feminino") && this.calcImc(s.getPeso(), s.getAltura()) > 30).count());

        document.put("percenteFem", this.calcPercentage(totalObesityFem, totalFem));

        return document;
    }

    private Integer calcPercentage(Integer totalObesity, Integer total) {
        double result = (double) totalObesity / total;
        result = result * 100;

        return (int) result;
    }

    private Document totalFilterByState(List<Donor> documents) {

        Document document = new Document();

        document.put("AC", 0);
        document.put("AL", 0);
        document.put("AP", 0);
        document.put("AM", 0);
        document.put("BA", 0);
        document.put("CE", 0);
        document.put("ES", 0);
        document.put("GO", 0);
        document.put("MA", 0);
        document.put("MT", 0);
        document.put("MS", 0);
        document.put("MG", 0);
        document.put("PA", 0);
        document.put("PB", 0);
        document.put("PR", 0);
        document.put("PE", 0);
        document.put("PI", 0);
        document.put("RJ", 0);
        document.put("RN", 0);
        document.put("RS", 0);
        document.put("RO", 0);
        document.put("RR", 0);
        document.put("SC", 0);
        document.put("SP", 0);
        document.put("SE", 0);
        document.put("TO", 0);
        document.put("DF", 0);

        documents.stream().forEach(s -> {
            switch (s.getEstado()) {
                case "AC":
                    document.put("AC", new AtomicInteger((Integer) document.getOrDefault("AC", 0)).incrementAndGet());
                    break;
                case "AL":
                    document.put("AL", new AtomicInteger((Integer) document.getOrDefault("AL", 0)).incrementAndGet());
                    break;
                case "AP":
                    document.put("AP", new AtomicInteger((Integer) document.getOrDefault("AP", 0)).incrementAndGet());
                    break;
                case "AM":
                    document.put("AM", new AtomicInteger((Integer) document.getOrDefault("AM", 0)).incrementAndGet());
                    break;
                case "BA":
                    document.put("BA", new AtomicInteger((Integer) document.getOrDefault("BA", 0)).incrementAndGet());
                    break;
                case "CE":
                    document.put("CE", new AtomicInteger((Integer) document.getOrDefault("CE", 0)).incrementAndGet());
                    break;
                case "ES":
                    document.put("ES", new AtomicInteger((Integer) document.getOrDefault("ES", 0)).incrementAndGet());
                    break;
                case "GO":
                    document.put("GO", new AtomicInteger((Integer) document.getOrDefault("GO", 0)).incrementAndGet());
                    break;
                case "MA":
                    document.put("MA", new AtomicInteger((Integer) document.getOrDefault("MA", 0)).incrementAndGet());
                    break;
                case "MT":
                    document.put("MT", new AtomicInteger((Integer) document.getOrDefault("MT", 0)).incrementAndGet());
                    break;
                case "MS":
                    document.put("MS", new AtomicInteger((Integer) document.getOrDefault("MS", 0)).incrementAndGet());
                    break;
                case "MG":
                    document.put("MG", new AtomicInteger((Integer) document.getOrDefault("MG", 0)).incrementAndGet());
                    break;
                case "PA":
                    document.put("PA", new AtomicInteger((Integer) document.getOrDefault("PA", 0)).incrementAndGet());
                    break;
                case "PB":
                    document.put("PB", new AtomicInteger((Integer) document.getOrDefault("PB", 0)).incrementAndGet());
                    break;
                case "PR":
                    document.put("PR", new AtomicInteger((Integer) document.getOrDefault("PR", 0)).incrementAndGet());
                    break;
                case "PE":
                    document.put("PE", new AtomicInteger((Integer) document.getOrDefault("PE", 0)).incrementAndGet());
                    break;
                case "PI":
                    document.put("PI", new AtomicInteger((Integer) document.getOrDefault("PI", 0)).incrementAndGet());
                    break;
                case "RJ":
                    document.put("RJ", new AtomicInteger((Integer) document.getOrDefault("RJ", 0)).incrementAndGet());
                    break;
                case "RN":
                    document.put("RN", new AtomicInteger((Integer) document.getOrDefault("RN", 0)).incrementAndGet());
                    break;
                case "RS":
                    document.put("RS", new AtomicInteger((Integer) document.getOrDefault("RS", 0)).incrementAndGet());
                    break;
                case "RO":
                    document.put("AC", new AtomicInteger((Integer) document.getOrDefault("RO", 0)).incrementAndGet());
                    break;
                case "RR":
                    document.put("RR", new AtomicInteger((Integer) document.getOrDefault("RR", 0)).incrementAndGet());
                    break;
                case "SC":
                    document.put("SC", new AtomicInteger((Integer) document.getOrDefault("SC", 0)).incrementAndGet());
                    break;
                case "SP":
                    document.put("SP", new AtomicInteger((Integer) document.getOrDefault("SP", 0)).incrementAndGet());
                    break;
                case "SE":
                    document.put("SE", new AtomicInteger((Integer) document.getOrDefault("SE", 0)).incrementAndGet());
                    break;
                case "TO":
                    document.put("TO", new AtomicInteger((Integer) document.getOrDefault("TO", 0)).incrementAndGet());
                    break;
                case "DF":
                    document.put("DF", new AtomicInteger((Integer) document.getOrDefault("DF", 0)).incrementAndGet());
                    break;
                default:
                    break;
            }
        });

        return document;
    }

    private Document avgImcByAgeGroup(List<Donor> documents) {

        Document document     = new Document();

        documents.stream().forEach(s -> {
            int idade  = Integer.parseInt(DateHelper.formatedDateYear(0, "YYYY")) - Integer.parseInt(DateHelper.formatDate(s.getDataNasc(), "YYYY"));

            document.put("zeroandten", new Document("total", 0).append("sumImc", 0.0));
            document.put("elevenandtwenty", new Document("total", 0).append("sumImc", 0.0));
            document.put("twentyoneandthirty", new Document("total", 0).append("sumImc", 0.0));
            document.put("overthirty", new Document("total", 0).append("sumImc", 0.0));

            if(idade >= 0 && idade <= 10) {
                document.put("zeroandten", new Document("total", new AtomicInteger((Integer) document.get("zeroandten", Document.class).getOrDefault("total", 0)).incrementAndGet())
                        .append("sumImc", document.get("zeroandten", Document.class).getDouble("sumImc") + this.calcImc(s.getPeso(), s.getAltura())));
            }else if(idade >= 11 && idade <= 20) {
                document.put("elevenandtwenty", new Document("total", new AtomicInteger((Integer) document.get("elevenandtwenty", Document.class).getOrDefault("total", 0)).incrementAndGet())
                        .append("sumImc", document.get("elevenandtwenty", Document.class).getDouble("sumImc") + this.calcImc(s.getPeso(), s.getAltura())));
            }else if(idade >= 21 && idade <= 30) {
                document.put("twentyoneandthirty", new Document("total", new AtomicInteger((Integer) document.get("twentyoneandthirty", Document.class).getOrDefault("total", 0)).incrementAndGet())
                        .append("sumImc", document.get("twentyoneandthirty", Document.class).getDouble("sumImc") + this.calcImc(s.getPeso(), s.getAltura())));
            }else {
                document.put("overthirty", new Document("total", new AtomicInteger((Integer) document.get("overthirty", Document.class).getOrDefault("total", 0)).incrementAndGet())
                        .append("sumImc", document.get("overthirty", Document.class).getDouble("sumImc") + this.calcImc(s.getPeso(), s.getAltura())));
            }

        });

        if(document.get("zeroandten", Document.class).getInteger("total") == 0 &&
            document.get("zeroandten", Document.class).getDouble("sumImc") == 0.0) {
            document.put("zeroandten", 0);
        }else {
            document.put("zeroandten", document.get("zeroandten", Document.class).getDouble("sumImc") / document.get("zeroandten", Document.class).getInteger("total"));
        }

        if(document.get("elevenandtwenty", Document.class).getInteger("total") == 0 &&
                document.get("elevenandtwenty", Document.class).getDouble("sumImc") == 0.0) {
            document.put("elevenandtwenty", 0);
        }else {
            document.put("elevenandtwenty", document.get("elevenandtwenty", Document.class).getDouble("sumImc") / document.get("elevenandtwenty", Document.class).getInteger("total"));
        }

        if(document.get("twentyoneandthirty", Document.class).getInteger("total") == 0 &&
                document.get("twentyoneandthirty", Document.class).getDouble("sumImc") == 0.0) {
            document.put("twentyoneandthirty", 0);
        }else {
            document.put("twentyoneandthirty", document.get("twentyoneandthirty", Document.class).getDouble("sumImc") / document.get("twentyoneandthirty", Document.class).getInteger("total"));
        }

        if(document.get("overthirty", Document.class).getInteger("total") == 0 &&
                document.get("overthirty", Document.class).getDouble("sumImc") == 0.0) {
            document.put("overthirty", 0);
        }else {
            document.put("overthirty", document.get("overthirty", Document.class).getDouble("sumImc") / document.get("overthirty", Document.class).getInteger("total"));
        }

        return document;
    }

    private double calcImc(Integer peso, Double altura) {
        return peso / (altura * altura);
    }

    public Response findAllDonors(int page, int size) {

        Response response = new Response();

        try {
            Pageable paging = PageRequest.of(page-1, size);

            long totalDonors = this.repository.count();

            Integer totalPages = (int) Math.ceil((double) totalDonors / size);

            Page<Donor> donorsPaged = this.repository.findAll(paging);

            List<Donor> allDonors = donorsPaged.getContent();

            List<Document> listDocs = new ArrayList<>();

            allDonors.stream().forEach(s -> listDocs.add(s.toDocument(s)));

            response.setSuccess(true);
            response.setResult(listDocs);
            response.setCurrentPage(page);
            response.setTotal(totalDonors);
            response.setTotalPages(totalPages);
            response.setLimit(size);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public void persistFile(MultipartFile file) throws IOException {
        File fileEx = new File("src/main/resources/data.json");

        if(!fileEx.exists()) {

            OutputStream os = new FileOutputStream("src/main/resources/data.json");
            InputStream inputStream = file.getInputStream();

            byte[] buffer = new byte[8 * 1024];

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            inputStream.close();
        }
    }


    public boolean findDonor(MultipartFile file) throws IOException {
        String json = new String(file.getBytes(), StandardCharsets.UTF_8);

        for(Document document : this.toListOfDocuments(json)) {
            Donor donor = this.repository.findByNome(document.getString("nome")).orElse(null);
            if(donor != null) return true;
        }
        return false;
    }
}
