package uk.co.sheffieldwebprogrammer.apacheagedemo.controller;

import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.sheffieldwebprogrammer.apacheagedemo.model.NodeEnum;
import uk.co.sheffieldwebprogrammer.apacheagedemo.model.RelationEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/enums")
public class EnumController {
    
    public class EnumsResponse {
        private String[] nodeEnums;
        private String[] relationEnums;

        public EnumsResponse(String[] nodeEnums, String[] relationEnums) {
            this.nodeEnums = nodeEnums;
            this.relationEnums = relationEnums;
        }

        public String[] getNodeEnums() {
            return nodeEnums;
        }

        public String[] getRelationEnums() {
            return relationEnums;
        }
    }

    @GetMapping
    public EnumsResponse getEnums() {
        String[] nodeEnums = Stream.of(NodeEnum.values())
                .map(Enum::name)
                .toArray(String[]::new);

        String[] relationEnums = Stream.of(RelationEnum.values())
            .map(Enum::name)
            .toArray(String[]::new);
        return new EnumsResponse(nodeEnums, relationEnums);
    }
    

}
