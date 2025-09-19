package com.micro.core.common;

import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 6/9/22 9:56 PM
 */
public class RandomName
{
    private static List<String> left = new ArrayList<>(Arrays.asList("admiring", "adoring", "agitated", "amazing", "angry", "awesome", "backstabbing",
            "berserk", "big", "boring", "clever", "cocky", "compassionate", "condescending", "cranky", "desperate", "determined", "distracted",
            "dreamy", "drunk", "ecstatic", "elated", "elegant", "evil", "fervent", "focused", "furious", "gigantic", "gloomy", "goofy", "grave",
            "happy", "high", "hopeful", "hungry", "infallible", "jolly", "jovial", "kickass", "lonely", "loving", "mad", "modest", "naughty",
            "nauseous", "nostalgic", "peaceful", "pedantic", "pensive", "prickly", "reverent", "romantic", "sad", "serene", "sharp", "sick",
            "silly", "sleepy", "small", "stoic", "stupefied", "suspicious", "tender", "thirsty", "tiny", "trusting", "zen"));
    private static List<String> right = new ArrayList<>(Arrays.asList(// Muhammad ibn Jābir al-Ḥarrānī al-Battānī was a founding father of astronomy. https://en.wikipedia.org/wiki/Mu%E1%B8%A5ammad\_ibn\_J%C4%81bir\_al-%E1%B8%A4arr%C4%81n%C4%AB\_al-Batt%C4%81n%C4%AB
            "albattani",
        
            // Frances E. Allen, became the first female IBM Fellow in 1989. In 2006, she became the first female recipient of the ACM's Turing Award. https://en.wikipedia.org/wiki/Frances\_E.\_Allen
            "allen",
        
            // June Almeida - Scottish virologist who took the first pictures of the rubella virus - https://en.wikipedia.org/wiki/June\_Almeida
            "almeida",
        
            // Maria Gaetana Agnesi - Italian mathematician, philosopher, theologian and humanitarian. She was the first woman to write a mathematics handbook and the first woman appointed as a Mathematics Professor at a University. https://en.wikipedia.org/wiki/Maria\_Gaetana\_Agnesi
            "agnesi",
        
            // Archimedes was a physicist, engineer and mathematician who invented too many things to list them here. https://en.wikipedia.org/wiki/Archimedes
            "archimedes",
        
            // Maria Ardinghelli - Italian translator, mathematician and physicist - https://en.wikipedia.org/wiki/Maria\_Ardinghelli
            "ardinghelli",
        
            // Aryabhata - Ancient Indian mathematician-astronomer during 476-550 CE https://en.wikipedia.org/wiki/Aryabhata
            "aryabhata",
        
            // Wanda Austin - Wanda Austin is the President and CEO of The Aerospace Corporation, a leading architect for the US security space programs. https://en.wikipedia.org/wiki/Wanda\_Austin
            "austin",
        
            // Charles Babbage invented the concept of a programmable computer. https://en.wikipedia.org/wiki/Charles\_Babbage.
            "babbage",
        
            // Stefan Banach - Polish mathematician, was one of the founders of modern functional analysis. https://en.wikipedia.org/wiki/Stefan\_Banach
            "banach",
        
            // John Bardeen co-invented the transistor - https://en.wikipedia.org/wiki/John\_Bardeen
            "bardeen",
        
            // Jean Bartik, born Betty Jean Jennings, was one of the original programmers for the ENIAC computer. https://en.wikipedia.org/wiki/Jean\_Bartik
            "bartik",
        
            // Laura Bassi, the world's first female professor https://en.wikipedia.org/wiki/Laura\_Bassi
            "bassi",
        
            // Alexander Graham Bell - an eminent Scottish-born scientist, inventor, engineer and innovator who is credited with inventing the first practical telephone - https://en.wikipedia.org/wiki/Alexander\_Graham\_Bell
            "bell",
        
            // Homi J Bhabha - was an Indian nuclear physicist, founding director, and professor of physics at the Tata Institute of Fundamental Research. Colloquially known as "father of Indian nuclear programme"- https://en.wikipedia.org/wiki/Homi\_J.\_Bhabha
            "bhabha",
        
            // Bhaskara II - Ancient Indian mathematician-astronomer whose work on calculus predates Newton and Leibniz by over half a millennium - https://en.wikipedia.org/wiki/Bh%C4%81skara\_II#Calculus
            "bhaskara",
        
            // Elizabeth Blackwell - American doctor and first American woman to receive a medical degree - https://en.wikipedia.org/wiki/Elizabeth\_Blackwell
            "blackwell",
        
            // Niels Bohr is the father of quantum theory. https://en.wikipedia.org/wiki/Niels\_Bohr.
            "bohr",
        
            // Kathleen Booth, she's credited with writing the first assembly language. https://en.wikipedia.org/wiki/Kathleen\_Booth
            "booth",
        
            // Anita Borg - Anita Borg was the founding director of the Institute for Women and Technology (IWT). https://en.wikipedia.org/wiki/Anita\_Borg
            "borg",
        
            // Satyendra Nath Bose - He provided the foundation for Bose–Einstein statistics and the theory of the Bose–Einstein condensate. - https://en.wikipedia.org/wiki/Satyendra\_Nath\_Bose
            "bose",
        
            // Evelyn Boyd Granville - She was one of the first African-American woman to receive a Ph.D. in mathematics; she earned it in 1949 from Yale University. https://en.wikipedia.org/wiki/Evelyn\_Boyd\_Granville
            "boyd",
        
            // Brahmagupta - Ancient Indian mathematician during 598-670 CE who gave rules to compute with zero - https://en.wikipedia.org/wiki/Brahmagupta#Zero
            "brahmagupta",
        
            // Walter Houser Brattain co-invented the transistor - https://en.wikipedia.org/wiki/Walter\_Houser\_Brattain
            "brattain",
        
            // Emmett Brown invented time travel. https://en.wikipedia.org/wiki/Emmett\_Brown (thanks Brian Goff)
            "brown",
        
            // Rachel Carson - American marine biologist and conservationist, her book Silent Spring and other writings are credited with advancing the global environmental movement. https://en.wikipedia.org/wiki/Rachel\_Carson
            "carson",
        
            // Subrahmanyan Chandrasekhar - Astrophysicist known for his mathematical theory on different stages and evolution in structures of the stars. He has won nobel prize for physics - https://en.wikipedia.org/wiki/Subrahmanyan\_Chandrasekhar
            "chandrasekhar",
        
            //Claude Shannon - The father of information theory and founder of digital circuit design theory. (https://en.wikipedia.org/wiki/Claude\_Shannon)
            "shannon",
        
            // Jane Colden - American botanist widely considered the first female American botanist - https://en.wikipedia.org/wiki/Jane\_Colden
            "colden",
        
            // Gerty Theresa Cori - American biochemist who became the third woman—and first American woman—to win a Nobel Prize in science, and the first woman to be awarded the Nobel Prize in Physiology or Medicine. Cori was born in Prague. https://en.wikipedia.org/wiki/Gerty\_Cori
            "cori",
        
            // Seymour Roger Cray was an American electrical engineer and supercomputer architect who designed a series of computers that were the fastest in the world for decades. https://en.wikipedia.org/wiki/Seymour\_Cray
            "cray",
        
            // This entry reflects a husband and wife team who worked together:
            // Joan Curran was a Welsh scientist who developed radar and invented chaff, a radar countermeasure. https://en.wikipedia.org/wiki/Joan\_Curran
            // Samuel Curran was an Irish physicist who worked alongside his wife during WWII and invented the proximity fuse. https://en.wikipedia.org/wiki/Samuel\_Curran
            "curran",
        
            // Marie Curie discovered radioactivity. https://en.wikipedia.org/wiki/Marie\_Curie.
            "curie",
        
            // Charles Darwin established the principles of natural evolution. https://en.wikipedia.org/wiki/Charles\_Darwin.
            "darwin",
        
            // Leonardo Da Vinci invented too many things to list here. https://en.wikipedia.org/wiki/Leonardo\_da\_Vinci.
            "davinci",
        
            // Edsger Wybe Dijkstra was a Dutch computer scientist and mathematical scientist. https://en.wikipedia.org/wiki/Edsger\_W.\_Dijkstra.
            "dijkstra",
        
            // Donna Dubinsky - played an integral role in the development of personal digital assistants (PDAs) serving as CEO of Palm, Inc. and co-founding Handspring. https://en.wikipedia.org/wiki/Donna\_Dubinsky
            "dubinsky",
        
            // Annie Easley - She was a leading member of the team which developed software for the Centaur rocket stage and one of the first African-Americans in her field. https://en.wikipedia.org/wiki/Annie\_Easley
            "easley",
        
            // Thomas Alva Edison, prolific inventor https://en.wikipedia.org/wiki/Thomas\_Edison
            "edison",
        
            // Albert Einstein invented the general theory of relativity. https://en.wikipedia.org/wiki/Albert\_Einstein
            "einstein",
        
            // Gertrude Elion - American biochemist, pharmacologist and the 1988 recipient of the Nobel Prize in Medicine - https://en.wikipedia.org/wiki/Gertrude\_Elion
            "elion",
        
            // Douglas Engelbart gave the mother of all demos: https://en.wikipedia.org/wiki/Douglas\_Engelbart
            "engelbart",
        
            // Euclid invented geometry. https://en.wikipedia.org/wiki/Euclid
            "euclid",
        
            // Leonhard Euler invented large parts of modern mathematics. https://de.wikipedia.org/wiki/Leonhard\_Euler
            "euler",
        
            // Pierre de Fermat pioneered several aspects of modern mathematics. https://en.wikipedia.org/wiki/Pierre\_de\_Fermat
            "fermat",
        
            // Enrico Fermi invented the first nuclear reactor. https://en.wikipedia.org/wiki/Enrico\_Fermi.
            "fermi",
        
            // Richard Feynman was a key contributor to quantum mechanics and particle physics. https://en.wikipedia.org/wiki/Richard\_Feynman
            "feynman",
        
            // Benjamin Franklin is famous for his experiments in electricity and the invention of the lightning rod.
            "franklin",
        
            // Galileo was a founding father of modern astronomy, and faced politics and obscurantism to establish scientific truth.  https://en.wikipedia.org/wiki/Galileo\_Galilei
            "galileo",
        
            // William Henry "Bill" Gates III is an American business magnate, philanthropist, investor, computer programmer, and inventor. https://en.wikipedia.org/wiki/Bill\_Gates
            "gates",
        
            // Adele Goldberg, was one of the designers and developers of the Smalltalk language. https://en.wikipedia.org/wiki/Adele\_Goldberg\_(computer\_scientist)
            "goldberg",
        
            // Adele Goldstine, born Adele Katz, wrote the complete technical description for the first electronic digital computer, ENIAC. https://en.wikipedia.org/wiki/Adele\_Goldstine
            "goldstine",
        
            // Shafi Goldwasser is a computer scientist known for creating theoretical foundations of modern cryptography. Winner of 2012 ACM Turing Award. https://en.wikipedia.org/wiki/Shafi\_Goldwasser
            "goldwasser",
        
            // James Golick, all around gangster.
            "golick",
        
            // Jane Goodall - British primatologist, ethologist, and anthropologist who is considered to be the world's foremost expert on chimpanzees - https://en.wikipedia.org/wiki/Jane\_Goodall
            "goodall",
        
            // Margaret Hamilton - Director of the Software Engineering Division of the MIT Instrumentation Laboratory, which developed on-board flight software for the Apollo space program. https://en.wikipedia.org/wiki/Margaret\_Hamilton\_(scientist)
            "hamilton",
        
            // Stephen Hawking pioneered the field of cosmology by combining general relativity and quantum mechanics. https://en.wikipedia.org/wiki/Stephen\_Hawking
            "hawking",
        
            // Werner Heisenberg was a founding father of quantum mechanics. https://en.wikipedia.org/wiki/Werner\_Heisenberg
            "heisenberg",
        
            // Jaroslav Heyrovský was the inventor of the polarographic method, father of the electroanalytical method, and recipient of the Nobel Prize in 1959. His main field of work was polarography. https://en.wikipedia.org/wiki/Jaroslav\_Heyrovsk%C3%BD
            "heyrovsky",
        
            // Dorothy Hodgkin was a British biochemist, credited with the development of protein crystallography. She was awarded the Nobel Prize in Chemistry in 1964. https://en.wikipedia.org/wiki/Dorothy\_Hodgkin
            "hodgkin",
        
            // Erna Schneider Hoover revolutionized modern communication by inventing a computerized telephone switching method. https://en.wikipedia.org/wiki/Erna\_Schneider\_Hoover
            "hoover",
        
            // Grace Hopper developed the first compiler for a computer programming language and  is credited with popularizing the term "debugging" for fixing computer glitches. https://en.wikipedia.org/wiki/Grace\_Hopper
            "hopper",
        
            // Frances Hugle, she was an American scientist, engineer, and inventor who contributed to the understanding of semiconductors, integrated circuitry, and the unique electrical principles of microscopic materials. https://en.wikipedia.org/wiki/Frances\_Hugle
            "hugle",
        
            // Hypatia - Greek Alexandrine Neoplatonist philosopher in Egypt who was one of the earliest mothers of mathematics - https://en.wikipedia.org/wiki/Hypatia
            "hypatia",
        
            // Yeong-Sil Jang was a Korean scientist and astronomer during the Joseon Dynasty; he invented the first metal printing press and water gauge. https://en.wikipedia.org/wiki/Jang\_Yeong-sil
            "jang",
        
            // Betty Jennings - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Jean\_Bartik
            "jennings",
        
            // Mary Lou Jepsen, was the founder and chief technology officer of One Laptop Per Child (OLPC), and the founder of Pixel Qi. https://en.wikipedia.org/wiki/Mary\_Lou\_Jepsen
            "jepsen",
        
            // Irène Joliot-Curie - French scientist who was awarded the Nobel Prize for Chemistry in 1935. Daughter of Marie and Pierre Curie. https://en.wikipedia.org/wiki/Ir%C3%A8ne\_Joliot-Curie
            "joliot",
        
            // Karen Spärck Jones came up with the concept of inverse document frequency, which is used in most search engines today. https://en.wikipedia.org/wiki/Karen\_Sp%C3%A4rck\_Jones
            "jones",
        
            // A. P. J. Abdul Kalam - is an Indian scientist aka Missile Man of India for his work on the development of ballistic missile and launch vehicle technology - https://en.wikipedia.org/wiki/A.\_P.\_J.\_Abdul\_Kalam
            "kalam",
        
            // Susan Kare, created the icons and many of the interface elements for the original Apple Macintosh in the 1980s, and was an original employee of NeXT, working as the Creative Director. https://en.wikipedia.org/wiki/Susan\_Kare
            "kare",
        
            // Mary Kenneth Keller, Sister Mary Kenneth Keller became the first American woman to earn a PhD in Computer Science in 1965. https://en.wikipedia.org/wiki/Mary\_Kenneth\_Keller
            "keller",
        
            // Har Gobind Khorana - Indian-American biochemist who shared the 1968 Nobel Prize for Physiology - https://en.wikipedia.org/wiki/Har\_Gobind\_Khorana
            "khorana",
        
            // Jack Kilby invented silicone integrated circuits and gave Silicon Valley its name. - https://en.wikipedia.org/wiki/Jack\_Kilby
            "kilby",
        
            // Maria Kirch - German astronomer and first woman to discover a comet - https://en.wikipedia.org/wiki/Maria\_Margarethe\_Kirch
            "kirch",
        
            // Donald Knuth - American computer scientist, author of "The Art of Computer Programming" and creator of the TeX typesetting system. https://en.wikipedia.org/wiki/Donald\_Knuth
            "knuth",
        
            // Sophie Kowalevski - Russian mathematician responsible for important original contributions to analysis, differential equations and mechanics - https://en.wikipedia.org/wiki/Sofia\_Kovalevskaya
            "kowalevski",
        
            // Marie-Jeanne de Lalande - French astronomer, mathematician and cataloguer of stars - https://en.wikipedia.org/wiki/Marie-Jeanne\_de\_Lalande
            "lalande",
        
            // Hedy Lamarr - Actress and inventor. The principles of her work are now incorporated into modern Wi-Fi, CDMA and Bluetooth technology. https://en.wikipedia.org/wiki/Hedy\_Lamarr
            "lamarr",
        
            // Leslie B. Lamport - American computer scientist. Lamport is best known for his seminal work in distributed systems and was the winner of the 2013 Turing Award. https://en.wikipedia.org/wiki/Leslie\_Lamport
            "lamport",
        
            // Mary Leakey - British paleoanthropologist who discovered the first fossilized Proconsul skull - https://en.wikipedia.org/wiki/Mary\_Leakey
            "leakey",
        
            // Henrietta Swan Leavitt - she was an American astronomer who discovered the relation between the luminosity and the period of Cepheid variable stars. https://en.wikipedia.org/wiki/Henrietta\_Swan\_Leavitt
            "leavitt",
        
            // Ruth Lichterman - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Ruth\_Teitelbaum
            "lichterman",
        
            // Barbara Liskov - co-developed the Liskov substitution principle. Liskov was also the winner of the Turing Prize in 2008. - https://en.wikipedia.org/wiki/Barbara\_Liskov
            "liskov",
        
            // Ada Lovelace invented the first algorithm. https://en.wikipedia.org/wiki/Ada\_Lovelace (thanks James Turnbull)
            "lovelace",
        
            // Auguste and Louis Lumière - the first filmmakers in history - https://en.wikipedia.org/wiki/Auguste\_and\_Louis\_Lumi%C3%A8re
            "lumiere",
        
            // Mahavira - Ancient Indian mathematician during 9th century AD who discovered basic algebraic identities - https://en.wikipedia.org/wiki/Mah%C4%81v%C4%ABra\_(mathematician)
            "mahavira",
        
            // Maria Mayer - American theoretical physicist and Nobel laureate in Physics for proposing the nuclear shell model of the atomic nucleus - https://en.wikipedia.org/wiki/Maria\_Mayer
            "mayer",
        
            // John McCarthy invented LISP: https://en.wikipedia.org/wiki/John\_McCarthy\_(computer\_scientist)
            "mccarthy",
        
            // Barbara McClintock - a distinguished American cytogeneticist, 1983 Nobel Laureate in Physiology or Medicine for discovering transposons. https://en.wikipedia.org/wiki/Barbara\_McClintock
            "mcclintock",
        
            // Malcolm McLean invented the modern shipping container: https://en.wikipedia.org/wiki/Malcom\_McLean
            "mclean",
        
            // Kay McNulty - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Kathleen\_Antonelli
            "mcnulty",
        
            // Lise Meitner - Austrian/Swedish physicist who was involved in the discovery of nuclear fission. The element meitnerium is named after her - https://en.wikipedia.org/wiki/Lise\_Meitner
            "meitner",
        
            // Carla Meninsky, was the game designer and programmer for Atari 2600 games Dodge 'Em and Warlords. https://en.wikipedia.org/wiki/Carla\_Meninsky
            "meninsky",
        
            // Johanna Mestorf - German prehistoric archaeologist and first female museum director in Germany - https://en.wikipedia.org/wiki/Johanna\_Mestorf
            "mestorf",
        
            // Marvin Minsky - Pioneer in Artificial Intelligence, co-founder of the MIT's AI Lab, won the Turing Award in 1969. https://en.wikipedia.org/wiki/Marvin\_Minsky
            "minsky",
        
            // Maryam Mirzakhani - an Iranian mathematician and the first woman to win the Fields Medal. https://en.wikipedia.org/wiki/Maryam\_Mirzakhani
            "mirzakhani",
        
            // Samuel Morse - contributed to the invention of a single-wire telegraph system based on European telegraphs and was a co-developer of the Morse code - https://en.wikipedia.org/wiki/Samuel\_Morse
            "morse",
        
            // Ian Murdock - founder of the Debian project - https://en.wikipedia.org/wiki/Ian\_Murdock
            "murdock",
        
            // Isaac Newton invented classic mechanics and modern optics. https://en.wikipedia.org/wiki/Isaac\_Newton
            "newton",
        
            // Florence Nightingale, more prominently known as a nurse, was also the first female member of the Royal Statistical Society and a pioneer in statistical graphics https://en.wikipedia.org/wiki/Florence\_Nightingale#Statistics\_and\_sanitary\_reform
            "nightingale",
        
            // Alfred Nobel - a Swedish chemist, engineer, innovator, and armaments manufacturer (inventor of dynamite) - https://en.wikipedia.org/wiki/Alfred\_Nobel
            "nobel",
        
            // Emmy Noether, German mathematician. Noether's Theorem is named after her. https://en.wikipedia.org/wiki/Emmy\_Noether
            "noether",
        
            // Poppy Northcutt. Poppy Northcutt was the first woman to work as part of NASA’s Mission Control. http://www.businessinsider.com/poppy-northcutt-helped-apollo-astronauts-2014-12?op=1
            "northcutt",
        
            // Robert Noyce invented silicone integrated circuits and gave Silicon Valley its name. - https://en.wikipedia.org/wiki/Robert\_Noyce
            "noyce",
        
            // Panini - Ancient Indian linguist and grammarian from 4th century CE who worked on the world's first formal system - https://en.wikipedia.org/wiki/P%C4%81%E1%B9%87ini#Comparison\_with\_modern\_formal\_systems
            "panini",
        
            // Ambroise Pare invented modern surgery. https://en.wikipedia.org/wiki/Ambroise\_Par%C3%A9
            "pare",
        
            // Louis Pasteur discovered vaccination, fermentation and pasteurization. https://en.wikipedia.org/wiki/Louis\_Pasteur.
            "pasteur",
        
            // Cecilia Payne-Gaposchkin was an astronomer and astrophysicist who, in 1925, proposed in her Ph.D. thesis an explanation for the composition of stars in terms of the relative abundances of hydrogen and helium. https://en.wikipedia.org/wiki/Cecilia\_Payne-Gaposchkin
            "payne",
        
            // Radia Perlman is a software designer and network engineer and most famous for her invention of the spanning-tree protocol (STP). https://en.wikipedia.org/wiki/Radia\_Perlman
            "perlman",
        
            // Rob Pike was a key contributor to Unix, Plan 9, the X graphic system, utf-8, and the Go programming language. https://en.wikipedia.org/wiki/Rob\_Pike
            "pike",
        
            // Henri Poincaré made fundamental contributions in several fields of mathematics. https://en.wikipedia.org/wiki/Henri\_Poincar%C3%A9
            "poincare",
        
            // Laura Poitras is a director and producer whose work, made possible by open source crypto tools, advances the causes of truth and freedom of information by reporting disclosures by whistleblowers such as Edward Snowden. https://en.wikipedia.org/wiki/Laura\_Poitras
            "poitras",
        
            // Claudius Ptolemy - a Greco-Egyptian writer of Alexandria, known as a mathematician, astronomer, geographer, astrologer, and poet of a single epigram in the Greek Anthology - https://en.wikipedia.org/wiki/Ptolemy
            "ptolemy",
        
            // C. V. Raman - Indian physicist who won the Nobel Prize in 1930 for proposing the Raman effect. - https://en.wikipedia.org/wiki/C.\_V.\_Raman
            "raman",
        
            // Srinivasa Ramanujan - Indian mathematician and autodidact who made extraordinary contributions to mathematical analysis, number theory, infinite series, and continued fractions. - https://en.wikipedia.org/wiki/Srinivasa\_Ramanujan
            "ramanujan",
        
            // Sally Kristen Ride was an American physicist and astronaut. She was the first American woman in space, and the youngest American astronaut. https://en.wikipedia.org/wiki/Sally\_Ride
            "ride",
        
            // Rita Levi-Montalcini - Won Nobel Prize in Physiology or Medicine jointly with colleague Stanley Cohen for the discovery of nerve growth factor (https://en.wikipedia.org/wiki/Rita\_Levi-Montalcini)
            "montalcini",
        
            // Dennis Ritchie - co-creator of UNIX and the C programming language. - https://en.wikipedia.org/wiki/Dennis\_Ritchie
            "ritchie",
        
            // Wilhelm Conrad Röntgen - German physicist who was awarded the first Nobel Prize in Physics in 1901 for the discovery of X-rays (Röntgen rays). https://en.wikipedia.org/wiki/Wilhelm\_R%C3%B6ntgen
            "roentgen",
        
            // Rosalind Franklin - British biophysicist and X-ray crystallographer whose research was critical to the understanding of DNA - https://en.wikipedia.org/wiki/Rosalind\_Franklin
            "rosalind",
        
            // Meghnad Saha - Indian astrophysicist best known for his development of the Saha equation, used to describe chemical and physical conditions in stars - https://en.wikipedia.org/wiki/Meghnad\_Saha
            "saha",
        
            // Jean E. Sammet developed FORMAC, the first widely used computer language for symbolic manipulation of mathematical formulas. https://en.wikipedia.org/wiki/Jean\_E.\_Sammet
            "sammet",
        
            // Carol Shaw - Originally an Atari employee, Carol Shaw is said to be the first female video game designer. https://en.wikipedia.org/wiki/Carol\_Shaw\_(video\_game\_designer)
            "shaw",
        
            // Dame Stephanie "Steve" Shirley - Founded a software company in 1962 employing women working from home. https://en.wikipedia.org/wiki/Steve\_Shirley
            "shirley",
        
            // William Shockley co-invented the transistor - https://en.wikipedia.org/wiki/William\_Shockley
            "shockley",
        
            // Françoise Barré-Sinoussi - French virologist and Nobel Prize Laureate in Physiology or Medicine; her work was fundamental in identifying HIV as the cause of AIDS. https://en.wikipedia.org/wiki/Fran%C3%A7oise\_Barr%C3%A9-Sinoussi
            "sinoussi",
        
            // Betty Snyder - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Betty\_Holberton
            "snyder",
        
            // Frances Spence - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Frances\_Spence
            "spence",
        
            // Richard Matthew Stallman - the founder of the Free Software movement, the GNU project, the Free Software Foundation, and the League for Programming Freedom. He also invented the concept of copyleft to protect the ideals of this movement, and enshrined this concept in the widely-used GPL (General Public License) for software. https://en.wikiquote.org/wiki/Richard\_Stallman
            "stallman",
        
            // Michael Stonebraker is a database research pioneer and architect of Ingres, Postgres, VoltDB and SciDB. Winner of 2014 ACM Turing Award. https://en.wikipedia.org/wiki/Michael\_Stonebraker
            "stonebraker",
        
            // Janese Swanson (with others) developed the first of the Carmen Sandiego games. She went on to found Girl Tech. https://en.wikipedia.org/wiki/Janese\_Swanson
            "swanson",
        
            // Aaron Swartz was influential in creating RSS, Markdown, Creative Commons, Reddit, and much of the internet as we know it today. He was devoted to freedom of information on the web. https://en.wikiquote.org/wiki/Aaron\_Swartz
            "swartz",
        
            // Bertha Swirles was a theoretical physicist who made a number of contributions to early quantum theory. https://en.wikipedia.org/wiki/Bertha\_Swirles
            "swirles",
        
            // Nikola Tesla invented the AC electric system and every gadget ever used by a James Bond villain. https://en.wikipedia.org/wiki/Nikola\_Tesla
            "tesla",
        
            // Ken Thompson - co-creator of UNIX and the C programming language - https://en.wikipedia.org/wiki/Ken\_Thompson
            "thompson",
        
            // Linus Torvalds invented Linux and Git. https://en.wikipedia.org/wiki/Linus\_Torvalds
            "torvalds",
        
            // Alan Turing was a founding father of computer science. https://en.wikipedia.org/wiki/Alan\_Turing.
            "turing",
        
            // Varahamihira - Ancient Indian mathematician who discovered trigonometric formulae during 505-587 CE - https://en.wikipedia.org/wiki/Var%C4%81hamihira#Contributions
            "varahamihira",
        
            // Sir Mokshagundam Visvesvaraya - is a notable Indian engineer.  He is a recipient of the Indian Republic's highest honour, the Bharat Ratna, in 1955. On his birthday, 15 September is celebrated as Engineer's Day in India in his memory - https://en.wikipedia.org/wiki/Visvesvaraya
            "visvesvaraya",
        
            // Christiane Nüsslein-Volhard - German biologist, won Nobel Prize in Physiology or Medicine in 1995 for research on the genetic control of embryonic development. https://en.wikipedia.org/wiki/Christiane\_N%C3%BCsslein-Volhard
            "volhard",
        
            // Marlyn Wescoff - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Marlyn\_Meltzer
            "wescoff",
        
            // Andrew Wiles - Notable British mathematician who proved the enigmatic Fermat's Last Theorem - https://en.wikipedia.org/wiki/Andrew\_Wiles
            "wiles",
        
            // Roberta Williams, did pioneering work in graphical adventure games for personal computers, particularly the King's Quest series. https://en.wikipedia.org/wiki/Roberta\_Williams
            "williams",
        
            // Sophie Wilson designed the first Acorn Micro-Computer and the instruction set for ARM processors. https://en.wikipedia.org/wiki/Sophie\_Wilson
            "wilson",
        
            // Jeannette Wing - co-developed the Liskov substitution principle. - https://en.wikipedia.org/wiki/Jeannette\_Wing
            "wing",
        
            // Steve Wozniak invented the Apple I and Apple II. https://en.wikipedia.org/wiki/Steve\_Wozniak
            "wozniak",
        
            // The Wright brothers, Orville and Wilbur - credited with inventing and building the world's first successful airplane and making the first controlled, powered and sustained heavier-than-air human flight - https://en.wikipedia.org/wiki/Wright\_brothers
            "wright",
        
            // Rosalyn Sussman Yalow - Rosalyn Sussman Yalow was an American medical physicist, and a co-winner of the 1977 Nobel Prize in Physiology or Medicine for development of the radioimmunoassay technique. https://en.wikipedia.org/wiki/Rosalyn\_Sussman\_Yalow
            "yalow",
        
            // Ada Yonath - an Israeli crystallographer, the first woman from the Middle East to win a Nobel prize in the sciences. https://en.wikipedia.org/wiki/Ada\_Yonath
            "yonath"));

    private static final String NAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-.@";

    static int leftlen = left.size();
    static int rightlen = right.size();
    public static String getRandomNameLR()
    {
        return String.format("%s.%s.%s", left.get(Utils.getRandomByRange(0, leftlen)), right.get(Utils.getRandomByRange(0, rightlen)), RandomStringUtils.randomAlphanumeric(4));
    }
    
    public static String getRandomNameRL()
    {
        return String.format("%s.%s.%s",  right.get(Utils.getRandomByRange(0, rightlen)),left.get(Utils.getRandomByRange(0, leftlen)), RandomStringUtils.randomAlphanumeric(4));
    }

    public static String getRandomName(int count)
    {
       return RandomStringUtils.random(count, 0, NAME_CHARS.length(), false, false, NAME_CHARS.toCharArray());
    }
}
