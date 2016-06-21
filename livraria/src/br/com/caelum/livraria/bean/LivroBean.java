package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;

@ManagedBean
@ViewScoped
public class LivroBean implements Serializable {

	private Livro livro = new Livro();
	private Integer autorId;

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Livro getLivro() {
		return livro;
	}

	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			// throw new RuntimeException("Livro deve ter pelo menos um
			// Autor.");
			FacesContext.getCurrentInstance().addMessage("autor",
					new FacesMessage("Livro deve ter pelo menos um Autor"));
		}
		
		if(livro.getId() != null && livro.getId().intValue() > 0) {
			System.out.println("Atualizando livro");
			new DAO<Livro>(Livro.class).atualiza(this.livro);
		}
		else {
			System.out.println("Salvando livro");
			new DAO<Livro>(Livro.class).adiciona(this.livro);
		}
		
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
		livro.adicionaAutor(autor);
	}

	public List<Autor> getAutoresDoLivro() {
		return livro.getAutores();
	}

	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("Deveria come�ar com 1"));
		}
	}

	public List<Livro> getListaLivros() {
		return new DAO<Livro>(Livro.class).listaTodos();
	}

	public String formAutor() {
		System.out.println("Chamando form autor");
		return "autor?faces-redirect=true";
	}

	public void removerLivro(Livro livro) {
		System.out.println("Removendo livro");
		new DAO<Livro>(Livro.class).remove(livro);
	}

	public void alterarLivro(Livro livro) {
		System.out.println("Buscando livro para altera��o");
		this.livro = livro;
	}

	public void removerAutor(Autor autor) {
		System.out.println("Removendo autor: " + autor.getNome());
		this.livro.getAutores().remove(autor);
	}
}
