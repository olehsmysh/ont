package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import org.semanticweb.owlapi.model.*;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import openllet.owlapi.explanation.PelletExplanation;
import openllet.owlapi.explanation.io.manchester.ManchesterSyntaxExplanationRenderer;
import org.semanticweb.owlapi.apibinding.OWLManager;

public class Main {
    private static final String file = "file:/Users/oliversmith/Desktop/ont/new.owl";
    private static final String NS = "http://www.semanticweb.org/oliver/ontologies/new.owl#";

    public static void main(String[] args) throws OWLOntologyCreationException, OWLException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntology(IRI.create(file));
        OWLDataFactory dataFactory = manager.getOWLDataFactory();

        OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(ontology);
        PelletExplanation explanationGenerator = new PelletExplanation(reasoner);

        ManchesterSyntaxExplanationRenderer renderer = new ManchesterSyntaxExplanationRenderer();
        PrintWriter out = new PrintWriter(System.out);
        renderer.startRendering(out);

        OWLIndividual angle = dataFactory.getOWLNamedIndividual(IRI.create(NS + "angle_C"));
        OWLDataPropertyExpression hasAngle = dataFactory.getOWLDataProperty(IRI.create(NS + "has_angle_of"));

        OWLAxiom axiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasAngle, angle, 105);
        System.out.println(axiom);
        System.out.println(ontology.getAxioms(AxiomType.SWRL_RULE));

        Set<Set<OWLAxiom>> explanations = explanationGenerator.getEntailmentExplanations(axiom);

        renderer.render(explanations);
        renderer.endRendering();
    }
}
