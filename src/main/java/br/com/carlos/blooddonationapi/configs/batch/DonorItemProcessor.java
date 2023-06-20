package br.com.carlos.blooddonationapi.configs.batch;

import br.com.carlos.blooddonationapi.entities.Donor;
import org.springframework.batch.item.ItemProcessor;

public class DonorItemProcessor implements ItemProcessor<Donor, Donor> {
    @Override
    public Donor process(Donor donor) throws Exception {
        return donor;
    }
}
