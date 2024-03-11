var stringToColour = function(str) {
    var hash = 0;
    for (var i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }
    var colour = '#';
    for (var i = 0; i < 3; i++) {
        var value = (hash >> (i * 8)) & 0xFF;
        colour += ('00' + value.toString(16)).substr(-2);
    }
    return colour;
}

var getLocalName = function(str) {
    return str.substr(Math.max(str.lastIndexOf('/'), str.lastIndexOf('#')) + 1);
}

var getPrefix = function(str) {
    return str.substr(0, Math.max(str.lastIndexOf('/'), str.lastIndexOf('#')));
}

var convertData = function(data) {
    var mapped = _.map(data, function(value, subject) {
        return _.map(value, function(value1, predicate) {
            return _.map(value1, function(object) {
                return [
                    subject,
                    predicate,
                    object.value
                ]
            })
        })
    });

    // Convert graph json to array of triples
    var triples = _.reduce(mapped, function(memo, el) {
        return memo.concat(el)
    }, []);
    triples = _.reduce(triples, function(memo, el) {
        return memo.concat(el)
    }, []);
    return triples;
}


// The RDFRank, nodes size is calculated according to RDFRank
var rankPredicate = "http://www.ontotext.com/owlim/RDFRank#hasRDFRank";

// Get type for a node to color nodes of the same type with the same color
var typePredicate = "http://www.openrdf.org/schema/sesame#directType";

// The location of a graphdb repo endpoint
var graphDBRepoLocation = 'http://localhost:8080/UNt/test';// 'http://factforge.net/repositories/ff-news';