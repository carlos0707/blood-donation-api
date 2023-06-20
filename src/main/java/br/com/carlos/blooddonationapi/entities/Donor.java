package br.com.carlos.blooddonationapi.entities;

import br.com.carlos.blooddonationapi.helper.DateHelper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.bson.Document;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DONOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Donor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "rg")
    private String rg;

    @Column(name = "data_nascimento")
    private String dataNasc;

    private Integer idade;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "mae")
    private String mae;

    @Column(name = "pai")
    private String pai;

    @Column(name = "email")
    private String email;

    @Column(name = "cep")
    private String cep;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "telefone_fixo")
    private String telefoneFixo;

    @Column(name = "celular")
    private String celular;

    @Column(name = "altura")
    private Double altura;

    @Column(name = "peso")
    private Integer peso;

    @Column(name = "tipo_sanguineo")
    private String tipoSanguineo;

    public Donor(Document document) {
        this.fromDocument(document);
    }

    private Donor fromDocument(Document document) {

        this.setNome(document.getString("nome"));
        this.setCpf(document.getString("cpf"));
        this.setRg(document.getString("rg"));
        this.setDataNasc(document.getString("data_nasc"));
        this.setIdade(Integer.parseInt(DateHelper.formatedDateYear(0, "YYYY")) - Integer.parseInt(DateHelper.formatDate(this.getDataNasc(), "YYYY")));
        this.setSexo(document.getString("sexo"));
        this.setMae(document.getString("mae"));
        this.setPai(document.getString("pai"));
        this.setEmail(document.getString("email"));
        this.setCep(document.getString("cep"));
        this.setEndereco(document.getString("endereco"));
        this.setNumero(document.getInteger("numero"));
        this.setBairro(document.getString("bairro"));
        this.setCidade(document.getString("cidade"));
        this.setEstado(document.getString("estado"));
        this.setTelefoneFixo(document.getString("telefone_fixo"));
        this.setCelular(document.getString("celular"));
        this.setAltura(document.getDouble("altura"));
        this.setPeso(document.getInteger("peso"));
        this.setTipoSanguineo(document.getString("tipo_sanguineo"));

        return this;
    }

    public Document toDocument(Donor donor) {
        Document document = new Document();

        if(this.nome != null && !this.nome.isEmpty()) document.put("name", this.nome);
        if(this.cpf != null && !this.cpf.isEmpty()) document.put("cpf", this.cpf);
        if(this.rg != null && !this.rg.isEmpty()) document.put("rg", this.rg);
        if(this.tipoSanguineo != null && !this.tipoSanguineo.isEmpty()) document.put("typeBlood", this.tipoSanguineo);

        return document;
    }

}
