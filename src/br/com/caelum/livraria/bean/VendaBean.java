package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.Venda;

@Named
@ViewScoped
public class VendaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	LivroDao livroDao;
	
	public BarChartModel getVendasModel() {
		
        BarChartModel model = new BarChartModel();
        model.setAnimate(true);
        model.setTitle("Vendas Realizadas");
        model.setLegendPosition("ne");
        
        
        // pegando o eixo X do gráfico e setando o título do mesmo
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Título");

        // pegando o eixo Y do gráfico e setando o título do mesmo
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
 
        ChartSeries vendaSerie = new ChartSeries();
        vendaSerie.setLabel("Vendas 2017");
        
        List<Venda> vendas = getVendas(1234);
        
        for (Venda venda : vendas){
        	vendaSerie.set(venda.getLivro().getTitulo(), venda.getQuantidade());
        }
        
        ChartSeries vendaSerie2 = new ChartSeries();
        vendaSerie.setLabel("Vendas 2016");
        
        vendas = getVendas(4321);
        
        for (Venda venda : vendas){
        	vendaSerie2.set(venda.getLivro().getTitulo(), venda.getQuantidade());
        }
        
        model.addSeries(vendaSerie);
        model.addSeries(vendaSerie2);
        
        return model;
    }
	
	public List<Venda> getVendas(long seed){
		List<Livro> livros = livroDao.listaTodos();
		List<Venda> vendas = new ArrayList<Venda>();
		
		Random random = new Random(seed);
		
		for (Livro livro : livros){
			Integer quantidade = random.nextInt(500);
			vendas.add(new Venda(livro, quantidade));
		}
		
		return vendas;
	}

}
