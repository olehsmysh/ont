package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import org.semanticweb.owlapi.model.*;
import com.clarkparsia.owlapi.explanation.PelletExplanation;
import com.clarkparsia.owlapi.explanation.io.manchester.ManchesterSyntaxExplanationRenderer;
import com.clarkparsia.owlapiv3.OWL;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class Main1 {
    private static final String file = "file:/Users/oliversmith/Desktop/ont/new.owl";
    private static final String NS = "http://www.semanticweb.org/oliver/ontologies/new.owl#";

    public static void main(String[] args) throws OWLOntologyCreationException, OWLException, IOException {
        PelletExplanation.setup();

        ManchesterSyntaxExplanationRenderer renderer = new ManchesterSyntaxExplanationRenderer();
        PrintWriter out = new PrintWriter(System.out);
        renderer.startRendering(out);

        OWLOntologyManager manager = OWL.manager;
        OWLOntology ontology = manager.loadOntology(IRI.create(file));
        OWLDataFactory dataFactory = manager.getOWLDataFactory();

        PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
        PelletExplanation expGen = new PelletExplanation(reasoner);

        OWLIndividual angle = OWL.Individual(NS + "angle_C");
        OWLDataPropertyExpression hasAngle = OWL.DataProperty(NS + "has_angle_of");

        OWLAxiom axiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasAngle, angle, 105);
        //System.out.println(axiom);
        //System.out.println(ontology.getAxioms(AxiomType.SWRL_RULE));

        Set<Set<OWLAxiom>> explanations = expGen.getEntailmentExplanations(axiom);

        renderer.render(explanations);
        renderer.endRendering();
    }
}