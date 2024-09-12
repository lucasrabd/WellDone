package br.com.fiap.WellDone.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.WellDone.model.Produto;
import br.com.fiap.WellDone.repository.ProdutoRep;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/index_produto")
public class ProdutoResource {

	@Autowired
	private ProdutoRep produtoRepository;

	@Operation(description = "Este serviço retorna todas as produtos da base de dados", summary = "Retorna todas as produtos", tags = "Consulta")
	@GetMapping(value = "/todos")
	public List<Produto> ConsultaTodos() {
		List<Produto> lista = produtoRepository.findAll();
		for (Produto i : lista) {
			i.add(linkTo(methodOn(ProdutoResource.class).findById(i.getProduto_id()))
					.withRel("Quer consultar a pessoa por ID? Acesse este link: "));
		}
		return lista;

		// return produtoRepository.findAll();
	}

	@Operation(description = "Este serviço retorna produtos por id", summary = "Retorna produto por id", tags = "Consulta")
	@GetMapping(value = "/{produto_id}")
	public Produto findById(@PathVariable Long produto_id) {
		Optional<Produto> produto = produtoRepository.findById(produto_id);

		produto.get().add(linkTo(methodOn(ProdutoResource.class).ConsultaTodos())
				.withRel("deseja consultar todos produtos? Acesse este link: "));
		produto.get().add(linkTo(methodOn(ProdutoResource.class).inserirProduto(null))
				.withRel("deseja inserir um produto? Acesse este link: "));
		produto.get().add(linkTo(methodOn(ProdutoResource.class).atualizarProduto(produto_id, null))
				.withRel("deseja atualizar um produto? Acesse este link: "));
		produto.get().add(linkTo(methodOn(ProdutoResource.class).apagarProduto(produto_id))
				.withRel("deseja remover um produto? Acesse este link: "));

		return produto.get();

		// return produtoRepository.findById(id).get();
	}

	@Operation(description = "Este serviço possibilita a inserção de produtos no banco de dados", summary = "Inerção de produtos", tags = "Inserção")
	@PostMapping()
	public Produto inserirProduto(@RequestBody Produto produto) {
		return produtoRepository.save(produto);
	}

	@Operation(description = "Este serviço possibilita a Remoção de produtos no banco de dados", summary = "Remoção de produtos", tags = "Remoção")
	@DeleteMapping(value ="/remove_produto/{id_produto}")
	public Produto apagarProduto(@PathVariable Long id_produto) {
		Produto p1 = produtoRepository.findById(id_produto).get();
		produtoRepository.deleteById(id_produto);
		return p1;
	}

	@Operation(description = "Este serviço possibilita a atualziação de produtos no banco de dados", summary = "Atualização de produtos", tags = "Atualização")
	@PutMapping(value ="/atualiza_produto/{id_produto}")
	public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id_produto,
			@RequestBody Produto produtoAtualizado) {
		return produtoRepository.findById(id_produto).map(produto -> {
			produto.setProduto_nome(produtoAtualizado.getProduto_nome());
			produto.setProduto_descricao(produtoAtualizado.getProduto_descricao());
			produto.setProduto_preco(produtoAtualizado.getProduto_preco());
			produto.setProduto_qtd_estoque(produtoAtualizado.getProduto_qtd_estoque());
			produto.setProduto_categoria(produtoAtualizado.getProduto_categoria());
			produto.setProduto_marca(produtoAtualizado.getProduto_marca());
			return ResponseEntity.ok(produtoRepository.save(produto));
		}).orElse(ResponseEntity.notFound().build());
	}
}