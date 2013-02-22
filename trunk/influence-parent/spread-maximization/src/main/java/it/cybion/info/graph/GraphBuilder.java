package it.cybion.info.graph;

import it.cybion.influencers.twitter.TwitterFacade;
import it.cybion.influencers.twitter.TwitterFacadeFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import twitter4j.TwitterException;

import com.google.gson.Gson;

public class GraphBuilder
{
	public static void main(String[] args) throws UnknownHostException
	{
		Long[] laPerlaFashio2800 = {50687788L,39342647L,90678987L,45178522L,44380640L,17791133L,42977615L,20230249L,74104063L,18462729L,540282330L,29265875L,57815830L,16528461L,14218186L,34276558L,15134144L,557369800L,52217624L,37541131L,96752877L,237369668L,169966744L,16361000L,90737374L,415866031L,19053841L,72950235L,96707728L,131139651L,44891815L,95914250L,308357008L,28726228L,80363405L,475067660L,186454150L,73990273L,16140210L,30057184L,22673496L,73905725L,23569067L,13513622L,46618055L,13253442L,14077417L,46164460L,89159223L,25328604L,77996375L,54849687L,27027436L,6362172L,113128320L,371410003L,28102509L,43165080L,28343521L,25740719L,36094197L,46164385L,29973284L,23767397L,401624644L,20261862L,305660827L,116142776L,16998587L,74579653L,222304263L,538146580L,67389707L,245740668L,51073532L,34465464L,19468559L,53205603L,59169292L,132779174L,69036183L,58617633L,64747368L,158443314L,81403284L,11311902L,24457050L,40966059L,81028629L,58302339L,7954742L,47934750L,40012675L,104618584L,104679765L,526465174L,79989712L,132014710L,44918420L,120420865L,81653415L,75298735L,70628816L,88938642L,109623025L,43288675L,14508983L,24772798L,105533852L,31941957L,137513192L,135260774L,35023641L,106496804L,24895193L,336070010L,69051984L,9197562L,38487795L,27471254L,98471838L,96953107L,80596286L,27202956L,16999240L,140582636L,26308185L,89055310L,51805112L,55018128L,25264216L,27486467L,91362496L,124684140L,263094903L,22130393L,103079978L,33386702L,208625717L,158366651L,17201409L,29766089L,22903511L,319004065L,16345196L,108998340L,18013015L,94487475L,44953198L,42248738L,6075612L,213663448L,421463888L,28137458L,26277246L,54911461L,137474465L,129460080L,144985251L,223102849L,15706176L,19903580L,28357463L,44068170L,68745357L,155427330L,98417540L,21194323L,97446202L,27470993L,154539349L,41229669L,26163076L,136036450L,20059279L,19329652L,21969797L,24609490L,276099334L,57227107L,18164552L,26709235L,156485102L,73170325L,121786787L,44415931L,100013281L,10938962L,39517821L,33335771L,19936573L,92263579L,46738143L,15203008L,266076017L,45850664L,15857528L,32538068L,24486735L,96055977L,16407145L,37134946L,104632613L,164691769L,184372968L,16782358L,110537858L,35940907L,25173006L,77316669L,85060995L,117577354L,38750386L,97516940L,148774349L,24244199L,80891330L,17233765L,234752867L,188928646L,15443909L,18395069L,35209661L,158054061L,52255083L,25495376L,43741079L,39439381L,106512656L,15029685L,121423180L,175098708L,267947445L,57314649L,95193915L,23554635L,51897998L,28762029L,40894526L,105284231L,92973001L,16016402L,78102460L,87245090L,29644051L,27095233L,48574708L,8251252L,108645127L,17792410L,396045481L,97376882L,106187295L,48734143L,108629823L,16123470L,64607572L,80471176L,15152696L,135573201L,35229156L,568328163L,45076121L,9813152L,17792299L,191493449L,31494682L,205940250L,15673317L,47414027L,5174191L,21177509L,164797526L,442008072L,187871662L,127660121L,24190981L,14076599L,25311310L,40786899L,80767232L,235256275L,22761773L,156671765L,132570901L,18479795L,19131498L,86872957L,328984225L,557388286L,371208866L,17000394L,120543473L,37245653L,61733886L,84402927L,48679760L,47050216L,7072022L,129319986L,26524882L,91550367L,222616308L,22834216L,108142902L,18894893L,30807912L,16173089L,16499252L,24789160L,74120913L,60253409L,18057550L,96042296L,280185870L,506640338L,230504476L,221815363L,20415405L,30857851L,26230244L,25234818L,15839601L,74007151L,67637837L,20873446L,97447368L,20993978L,32876615L,28823223L,74129769L,124509213L,515551148L,255541028L,178111058L,27167403L,43880330L,22677473L,19749782L,56670359L,24253537L,250273348L,48897669L,104804023L,216303057L,17961592L,87218499L,21072245L,43523351L,66864935L,107492548L,36204683L,100074465L,94326934L,191775575L,94097845L,46178833L,65946149L,28155467L,186211223L,100584561L,101105818L,30502699L,38598545L,253970930L,19399719L,122307661L,505102305L,38022004L,18334825L,24316133L,141533820L,16193578L,33929978L,104570948L,45634697L,334740324L,109011397L,17788951L,158720573L,32208569L,419743304L,31253381L,24862205L,76447685L,21655428L,220969550L,39762246L,20581930L,29279164L,97045628L,134393472L,244632403L,89781256L,108908254L,97081048L,18690342L,104852697L,147149348L,25326324L,74577799L,20457703L,159825515L,102641804L,42039230L,13438512L,127629304L,450852010L,62026711L,26210679L,21165840L,20138182L,237196824L,37525965L,24132884L,23509283L,19661595L,128755257L,77011894L,11806782L,73107271L,545269172L,102893831L,73767295L,95929547L,28032657L,14603517L,29538908L,68805078L,20092734L,207159257L,774339403L,50138875L,21530096L,34124284L,28403546L,18742695L,128311764L,20277147L,88166048L,16266542L,101249366L,16440976L,442255608L,113448114L,514703181L,21239274L,164344072L,75327076L,61471971L,88971852L,102079657L,41456714L,179899339L,74459296L,145529509L,82224275L,274111342L,20697564L,3809211L,49986071L,19626782L,102113391L,71104690L,240614050L,20506424L,30702034L,29226212L,122095571L,130765407L,19342686L,91086635L,105613068L,37708658L,64523749L,30942281L,39076319L,33652071L,11647252L,40758258L,398955526L,111141904L,20138565L,8261872L,22866353L,115473930L,96058415L,107618296L,21654975L,97547761L,20076912L,45299330L,15225275L,17280644L,52904219L,814143362L,130114910L,165869174L,123626078L,115167053L,84080072L,89781784L,23871904L,18408856L,16124348L,57645476L,327637317L,85623731L,21707472L,417876480L,319107995L,23527407L,347627748L,109625036L,67407753L,37631079L,101039294L,19695807L,18214820L,50667940L,36190684L,105815050L,34573935L,75637034L,15461469L,47109548L,171711123L,152551783L,295147188L,23091551L,11977082L,62134788L,71740498L,38380691L,42497735L,305516112L,135977853L,95912525L,29232633L,751490216L,80273286L,17840259L,128296739L,15912441L,23153164L,37417059L,18197232L,372235843L,74629519L,15581706L,56809039L,19660870L,16102452L,15153375L,40048686L,15473779L,14945719L,270989536L,60037350L,22236628L,40432057L,158470405L,43756808L,105597862L,21377411L,132653134L,36651465L,207525701L,19955454L,22936333L,287694284L,97767268L,185417603L,141116843L,517644851L,190992702L,17933629L,19157750L,219323785L,21429094L,29478586L,34171574L,70383427L,72968568L,14369667L,20876047L,28635233L,23184598L,103828872L,38305243L,52815881L,71352734L,311727193L,27532954L,74630902L,37491839L,95867448L,6277652L,55078157L,28499574L,19082081L,110222846L,15383636L,24363358L,61700238L,205887432L,17842021L,18392906L,70793324L,33333677L,190806720L,208209139L,80715966L,425047011L,54710954L,20276151L,70647454L,88087921L,178529814L,30685781L,16317543L,75220371L,15582386L,21290030L,29659145L,22480873L,25071326L,13935482L,26887501L,17060175L,76936244L,20858775L,20962569L,140984231L,107295759L,47889512L,219720673L,82085616L,80042675L,32346324L,76017958L,55109108L,82518787L,13935362L,24565762L,64522637L,64019701L,86821971L,87879473L,20030757L,47916327L,40415424L,21133327L,33197471L,109047389L,107435285L,188133806L,89904293L,388911506L,82310285L,111033913L,25371535L,121089818L,17600124L,510142068L,48805997L,19219326L,107188317L,74964756L,50029392L,33320626L,94948404L,60970661L,15911016L,49006996L,14905731L,243609668L,20962927L,508702945L,101815155L,46894899L,97079503L,23322750L,328998441L,47376910L,242430921L,16578383L,218605514L,63346029L,26324991L,17565330L,61031431L,84170618L,18462653L,81840180L,18072830L,33091142L,164733758L,78115279L,247458352L,408724298L,30180137L,25283065L,128388302L,32631956L,36451558L,97045464L,223172743L,119846995L,600116289L,80592945L,55690883L,46038391L,27688800L,25621742L,23276645L,147044082L,62912260L,127208319L,43785576L,94313686L,187460051L,388177657L,93890712L,32052223L,105249032L,766702746L,29830859L,67718647L,39358580L,21455385L,31479238L,18431710L,80889392L,21776040L,232994730L,292149962L,21007542L,73761278L,39875521L,250069526L,17771860L,107401246L,24161758L,50499245L,18812419L,119513889L,20863936L,363750966L,132937233L,26208313L,14376465L,23939507L,115450146L,132371328L,14407655L,15790423L,29066530L,191520022L,249697533L,26147687L,15537443L,41800488L,95814242L,16434905L,68292695L,18198687L,20863870L,21406441L,42717310L,17461964L,393484989L,19395367L,60796720L,765643020L,52983468L,80149190L,109001850L,17072286L,27214346L,49239873L,73606734L,251669628L,107203273L,127731092L,99726170L,28194129L,83860541L,22669493L,111061267L,16435117L,114146114L,154639409L,24767201L,130936828L,129552720L,61790306L,212581062L,14237091L,50479751L,17367774L,19823088L,122311567L,105549520L,182577456L,16664213L,133412105L,119130254L,88445549L,23434479L,20939820L,53339564L,43138521L,108244446L,231479317L,29571578L,82852110L,64586891L,21753107L,125075583L,120461604L,107609511L,116017433L,88256473L,123949984L,24212069L,14167566L,30948824L,105599936L,167878804L,158851030L,46229701L,30352167L,91692921L,123203612L,21812094L,136928830L,18307364L,799964024L,209620288L,47072454L,365267874L,63668850L,99920862L,100009032L,20785274L,57455938L,120354956L,18238186L,76423275L,64241863L,14874361L,44411854L,150744763L,39266609L,167556923L,20350336L,108007963L,84320285L,74849509L,15883371L,42912195L,26096827L,646193L,190493607L,36536178L,249419283L,14718073L,39773829L,35250002L,278429196L,185149826L,19729003L,102168130L,119360353L,110216081L,237544893L,135108770L,127517292L,20070905L,94094414L,69071514L,16795267L,136462166L,54779428L,109299385L,196488420L,74259084L,31119337L,54288013L,250631802L,26018039L,138480810L,44699444L,21110968L,327631234L,130522705L,359788306L,56181737L,38367390L,390603220L,53983197L,15641148L,66249927L,21857559L,24142683L,88531430L,31668723L,44592558L,75085858L,240192691L,19967371L,100170236L,122117323L,43952326L,14934818L,27335801L,104921218L,132063963L,237947094L,20908109L,27198017L,358447643L,20266978L,58971451L,27014998L,39937191L,56398465L,221330121L,28826819L,244696438L,28882024L,58192095L,26418029L,48358776L,95549102L,67407919L,75751736L,87057610L,16682615L,132920895L,16813178L,3819511L,74524430L,187585908L,316308465L,132496698L,73685826L,211800689L,19594580L,120508715L,85137221L,14305711L,296901633L,46276487L,87162389L,24247962L,28618120L,187481722L,17199018L,87943029L,23329776L,42594895L,326454732L,377450220L,311290000L,10112122L,21675590L,119343578L,149042549L,22991137L,33044614L,142440072L,86158680L,22652957L,63515350L,252934983L,191076232L,27381858L,17128252L,55237325L,364351395L,27855950L,143062624L,60531346L,20419035L,19396219L,60136905L,89060990L,54673963L,21476507L,77081218L,15307166L,113307971L,16952113L,27769595L,14670581L,97981097L,115098336L,120708882L,21690979L,458650318L,19480822L,102549370L,70302524L,69567370L,8223872L,63118969L,122437280L,102446093L,16417854L,47968723L,20634777L,367504332L,21162077L,18099373L,119670712L,582928511L,104334159L,35531539L,34002050L,130858876L,72208512L,396100492L,52251346L,16064920L,33383670L,118749374L,142439905L,373636792L,71405003L,20052183L,149093294L,274745143L,66513197L,122700029L,82273294L,27981687L,96940759L,15692086L,82071817L,131649713L,74558689L,557401263L,39462783L,93892686L,135848214L,22080953L,138714078L,70996520L,127905155L,94894512L,14460964L,28702789L,69163240L,37364053L,69072737L,46337679L,72318857L,35028765L,28973196L,299304695L,269966887L,27840687L,91158254L,87727513L,20476522L,124127747L,110697244L,47922572L,94530136L,19967728L,14305810L,58262011L,98777477L,83921725L,35866050L,10692902L,101716473L,24599135L,146459814L,222066541L,85186941L,14306001L,54180264L,246839321L,4251531L,44133183L,118135894L,73164950L,18720323L,269897088L,101508714L,186774426L,43706206L,44715938L,234773932L,202560344L,19698688L,34437235L,46789927L,37981738L,485795109L,44102112L,176499816L,33847970L,81415731L,118714554L,24671904L,268926386L,37900374L,20298371L,78310879L,66841858L,21340144L,174613484L,342926273L,191562252L,96874435L,24368318L,20523584L,91324330L,225469865L,27844479L,25410931L,43568730L,59786846L,154117926L,93224675L,17180432L,83330202L,343454434L,27947085L,9635662L,343276614L,146387579L,88253999L,595783277L,75153961L,63452288L,233291471L,468200862L,26432422L,70347591L,115064725L,43849802L,25253558L,161743500L,31165191L,327741331L,39601084L,15325349L,101826781L,28425024L,261168495L,68808870L,555878533L,69569647L,92960597L,25118078L,140397922L,99935436L,14181054L,382236526L,48484036L,17758889L,304551287L,41223695L,111284269L,26864652L,46087596L,26240839L,19047781L,101581618L,230776510L,139845669L,53704777L,52188732L,94388895L,16648907L,372214161L,23055974L,156720988L,116413745L,26518042L,98670650L,27792314L,21213315L,102672674L,335211972L,245738885L,101390434L,93452741L,32810077L,35285062L,15485892L,36050076L,257265872L,74447856L,263334532L,232872974L,18254974L,6609862L,43248461L,107201184L,77024948L,82105499L,84678262L,40031983L,23003275L,111059640L,88113853L,76073936L,26222786L,39580939L,373421895L,109000289L,141069549L,34780958L,207397333L,70766243L,157338029L,82885530L,327388578L,159434582L,198370840L,140237975L,17417449L,166850874L,15777764L,159525899L,102742328L,255984815L,379498535L,58852624L,121256265L,91159447L,155592935L,34702063L,131282426L,123588556L,56152217L,25028891L,100057386L,34176258L,37764984L,55678606L,42405529L,249386756L,24129519L,243285809L,104933685L,100247619L,216074765L,15396470L,244525391L,242886133L,12136802L,20352155L,25411116L,18977316L,36065951L,19727067L,122773175L,42597758L,23875941L,21270402L,260024260L,21320186L,23875913L,155593023L,25976668L,26504581L,29161287L,24389566L,21060044L,263101262L,25135762L,155623492L,96539031L,26007042L,63608892L,26910562L,28212204L,90474911L,531883217L,180752808L,16067827L,17020624L,62473573L,21339141L,19744496L,453182549L,35459922L,39114914L,45895324L,67147643L,25201786L,80830914L,148325018L,209066958L,20493490L,18641695L,67965757L,348967777L,190523197L,77536813L,372215028L,95991908L,16115293L,94723121L,26159552L,279730184L,19544124L,23400538L,277105566L,71558562L,60533526L,25461773L,117056304L,19777635L,41630892L,26239721L,89776420L,161309159L,26005761L,17922436L,95863651L,64544431L,22576925L,36753655L,249178508L,44658875L,18721912L,27047807L,76526980L,17681365L,81378305L,17768619L,169680125L,18702618L,205339205L,23327390L,32688895L,190143885L,38401894L,34243239L,17630941L,58233294L,83635245L,273966586L,1879831L,33399313L,21459323L,99590931L,22871862L,31364559L,223636148L,106503462L,28585454L,36396565L,51193363L,72568426L,16261347L,29360173L,96890534L,156385432L,161652920L,330259624L,155981934L,47330647L,226895703L,99815664L,114812762L,34191123L,21564069L,84217993L,185738132L,23599214L,22576587L,7667502L,16144555L,17665068L,27093557L,111130056L,108999240L,23816599L,7788922L,173944483L,19653756L,15010370L,97216379L,85628522L,59459565L,54116618L,17370385L,19593045L,39740329L,22378249L,110184300L,83387362L,18993965L,261031595L,21060775L,33906739L,85275438L,221382131L,55040951L,32827381L,213076483L,118701543L,107307658L,58462957L,26346421L,621995600L,57317105L,14048129L,506882611L,32847964L,76993176L,153656769L,72017123L,15855013L,76993267L,42963778L,35564335L,109972863L,252742126L,218437887L,190633170L,188076134L,26628035L,52750832L,24826304L,18341295L,82214622L,234868626L,65478056L,30323900L,15196388L,105509165L,138468913L,113336593L,57272246L,103396327L,17495194L,74864030L,76247704L,42616122L,153474021L,197720024L,13297892L,46445414L,38834379L,107448565L,10490932L,27016235L,90984732L,29175089L,41149231L,137148806L,32373588L,127635597L,186515230L,119302162L,59876274L,167024449L,31006645L,22615940L,138524111L,19546942L,128150816L,15577027L,39474711L,52176912L,17909404L,84836429L,94249639L,308442339L,18645376L,160700592L,28535390L,21341353L,29422522L,112324931L,22948575L,22305195L,111498103L,18125041L,109036091L,85633816L,240848093L,101228081L,19789726L,23035240L,144400550L,249234624L,408592636L,78149965L,478699623L,193117243L,22631299L,26981599L,66447704L,53568512L,50833172L,41130678L,94587951L,450036261L,21769752L,37975686L,225190303L,17084945L,607944482L,65531375L,61880862L,23423547L,53490284L,69336058L,46401496L,19671127L,75073990L,104443368L,22770708L,84663909L,15479966L,11319582L,38222344L,20587833L,196634319L,15160573L,51749609L,259581901L,107077377L,771306942L,164476204L,106877485L,346687726L,35279871L,18491250L,60404690L,578434413L,276650601L,20465587L,47067387L,17865050L,19459455L,16252512L,25007375L,49165016L,196514953L,26099765L,27123664L,90454537L,23346816L,43242319L,41338503L,212846330L,22351088L,532242626L,22243019L,266187076L,153974223L,81038005L,39738600L,32269217L,307886770L,247015012L,29686163L,113869753L,72474009L,101965634L,121875698L,51221274L,38222128L,29775563L,135141037L,84395206L,92367751L,104521711L,70394683L,91995170L,62678032L,33398297L,234348782L,414658366L,16929349L,437047177L,33241430L,308520976L,107808886L,22701235L,65267407L,99131926L,230015807L,97585570L,112469068L,34258758L,35792246L,21839649L,67957237L,152142266L,42645333L,191889245L,829400197L,38434322L,131302950L,89086285L,414036890L,24322323L,46241729L,55315511L,18186303L,27308695L,21575573L,107200264L,35807690L,459730785L,107887647L,179112086L,92786140L,81228586L,45798009L,39985633L,57700447L,14209625L,189565993L,23438773L,56418949L,33990276L,274446779L,64440226L,34534378L,106601572L,212256137L,141926328L,59203749L,71363649L,297062257L,28252099L,18770287L,93002132L,557607236L,33398564L,103938599L,173988421L,427913952L,24572141L,107216109L,455254177L,26283645L,18172531L,80371182L,82925633L,19335587L,19928855L,17380545L,119594375L,14179410L,104221084L,19322840L,63234032L,20828409L,90608493L,14241704L,9265172L,66458932L,88718617L,28426624L,23081128L,352972750L,109310558L,97035816L,81194751L,26967436L,54203238L,94698895L,19010183L,107684088L,146203920L,23990165L,302247062L,113405068L,27938708L,35773072L,42551467L,49432899L,27072479L,177869612L,19963431L,40106169L,118844944L,40718259L,21591378L,111710640L,250125503L,91495941L,19000184L,132094229L,17224875L,34364058L,18203388L,158961733L,174014948L,52633661L,17207345L,109154652L,29405930L,23740932L,77107389L,18823232L,21279256L,35947725L,23049506L,63371288L,94417059L,124777527L,29484542L,30399129L,84577538L,40700895L,48966913L,67606347L,131301578L,59768449L,16430292L,55315155L,36601355L,45644745L,27969299L,47491555L,34301869L,20410266L,90625787L,19292133L,147725700L,187204824L,109842963L,449012014L,3266791L,39660271L,121017998L,95877744L,20145316L,18083627L,34892616L,109784120L,86352107L,216930502L,204491797L,37182573L,80491988L,43142602L,27479963L,118905225L,22896028L,44546621L,72167082L,38697940L,240679913L,118935599L,18854640L,26811823L,326359913L,103324394L,22024974L,53603223L,82375422L,210885668L,33570922L,94404302L,56565666L,145582395L,41666584L,374619583L,17907726L,105153857L,17881845L,80648872L,16181819L,50475775L,22700231L,21204093L,366962083L,82344721L,54096848L,40549610L,164771178L,15714876L,21713274L,29590915L,21663524L,35314879L,30601526L,91725964L,36103312L,25092727L,39768127L,381796771L,43298629L,23760251L,33920830L,161166265L,150382168L,28094359L,19106562L,29324353L,31405725L,14915383L,20782765L,22995171L,22666549L,105604455L,372054905L,8539922L,18999687L,21856096L,96187677L,248902044L,130200311L,35878439L,16806174L,85634050L,95739281L,48306394L,377634121L,19881803L,97035597L,251322323L,34283893L,151825436L,38013204L,101845813L,333759298L,256812467L,21553672L,182940890L,47459700L,17330053L,116487400L,45485382L,22981451L,128288318L,75318687L,18608216L,310709045L,14829076L,84682536L,20406956L,58346724L,129942852L,29435298L,76271567L,39084522L,43122620L,121432912L,259011186L,99556442L,106316617L,512939265L,87904708L,120516111L,41450148L,37180126L,28412286L,271944538L,21994370L,19756746L,50369481L,184835930L,140905379L,121574345L,39520685L,29081110L,15635123L,24155818L,27771065L,238262905L,206640085L,51945792L,106524569L,17745952L,103369438L,115748758L,27664173L,24800747L,35561054L,109296606L,54905177L,23518797L,115968592L,23237206L,20441634L,97405176L,76209589L,14774799L,22227375L,26086612L,78021221L,15482079L,44682832L,180809012L,39797861L,48065146L,15252906L,365317112L,53042982L,101839591L,34007249L,6034812L,102902750L,15885551L,19074134L,24675869L,620322972L,97713052L,92304235L,20177423L,111583939L,21889141L,53646811L,23067972L,92427587L,213623489L,45721954L,120550671L,159599555L,32837246L,24124842L,116695119L,351038947L,20008549L,14881749L,22786694L,33291343L,25096109L,191856490L,103602815L,81122694L,35233944L,7092102L,15163383L,181364071L,20779908L,28316135L,14196675L,133366851L,75993163L,196636064L,41648381L,106445504L,39754006L,126719989L,375100606L,157994424L,303621630L,62523069L,68559218L,473883807L,19495981L,62468992L,77708386L,20130921L,260990471L,20605458L,150188273L,33587619L,107798154L,31367569L,174018274L,36659889L,112593696L,61975526L,183734502L,15651844L,110001956L,827056640L,44250865L,287702899L,18329283L,20797012L,129943155L,274656986L,124215842L,69286914L,278128809L,447866255L,34332747L,267350660L,81849972L,41574260L,77172117L,26715749L,18647765L,141893369L,6234712L,26774034L,29341123L,103074395L,14828973L,17191788L,101455033L,125214706L,105727871L,85930729L,35047851L,128211880L,318601444L,17850018L,68436718L,52299041L,21017737L,105986533L,248490741L,72875456L,34645116L,272611369L,22010765L,21715659L,38115543L,63629416L,44849431L,74322800L,52528682L,23131595L,11100902L,195441534L,228379737L,104828583L,23563460L,140851804L,224132493L,16824090L,20588602L,26033281L,110143675L,89353408L,152048775L,60900141L,212770721L,161679576L,19914228L,90453108L,26173060L,99900375L,33658000L,26774882L,101364926L,109615744L,20814994L,9772732L,17863930L,85826164L,549470860L,89742312L,228432756L,15107898L,19549961L,17209624L,104766847L,72611322L,51752797L,99115453L,200156697L,24585964L,296472507L,46727381L,99572863L,399162174L,67119922L,412346254L,41182951L,22634113L,20815112L,62609081L,318708952L,92488375L,106538770L,84909726L,18804413L,106461507L,168566715L,18438908L,21156007L,23238245L,132888646L,18020342L,20099374L,8192222L,193457807L,21975963L,14177590L,28750091L,19212009L,239777975L,24922092L,66476097L,28317204L,18293184L,22503680L,20225875L,55029535L,72511061L,72304469L,14351070L,25079004L,20481548L,78022296L,87055991L,21730932L,62504242L,46666433L,24643679L,45571293L,96824115L,78643537L,13822082L,278708734L,125133412L,97383279L,25626894L,118582482L,155986104L,19670919L,60261362L,574396774L,42646261L,53615551L,301966948L,18683376L,107946541L,30017352L,82683258L,145722086L,16614021L,15989014L,369743200L,140206643L,107218468L,15162193L,98377183L,31671186L,98338641L,106299748L,110144063L,89912935L,14953151L,148092550L,47263779L,45553916L,21308332L,498790808L,172915470L,28689001L,50400933L,310817016L,36827798L,38481162L,574598418L,78519374L,212785163L,15697472L,369426625L,78427095L,45936861L,256498570L,86494166L,22647187L,49519643L,29257521L,103726512L,366673433L,21994730L,124095997L,14810463L,164803822L,28332178L,44926186L,100071234L,24154566L,40935821L,16600706L,104219272L,9101222L,31407693L,22009958L,41894068L,21554575L,16885747L,78332815L,74762509L,121280699L,196092679L,26388384L,151122949L,50766886L,97209255L,353259092L,162069651L,106104242L,23683840L,29137655L,120487424L,18451290L,120631117L,16890261L,39665188L,583977237L,18495248L,158407845L,102385517L,154065662L,19923638L,182094724L,73136614L,20933165L,91436809L,83076751L,17779550L,186246720L,21570885L,98941731L,357850742L,17375533L,15936494L,15667687L,94383392L,66436284L,155635001L,115367373L,169147795L,138549574L,15338492L,20716119L,31215751L,30863036L,619553L,151404886L,84545911L,19693022L,61150091L,18635959L,23621908L,409918798L,163340205L,67816451L,11037952L,37754403L,126391948L,119430759L,328167568L,20452058L,16495205L,88756689L,98079220L,72029697L,18138157L,73572308L,21683202L,19217191L,25828681L,24308932L,157826142L,84795702L,250590405L,23112981L,25276419L,85992458L,94942261L,109120817L,107844570L,40302362L,19247844L,16325986L,24881469L,108311018L,19127095L,24188699L,462403479L,113300666L,88696746L,46750483L,80933264L,20328080L,324877851L,107471620L,16908146L,33915734L,23305137L,144461529L,26494583L,57188113L,411002830L,130584976L,63228090L,41249630L,95683629L,207477965L,55519577L,103919294L,342195828L,511274326L,47477421L,31277095L,23358463L,20148567L,83520370L,29013669L,361330973L,40844176L,69575944L,19784944L,24569636L,51155230L,60417388L,17097782L,1228121L,117081665L,22541903L,21155579L,15632759L,6825532L,31584720L,308716304L,56546562L,146588724L,26885308L,377378864L,29717312L,61876481L,289234427L,101961614L,23564864L,107363454L,255055007L,342807201L,20148394L,57249277L,43856360L,21571178L,26200575L,24085297L,103650701L,88714086L,56330924L,28158240L,77731923L,79271435L,97427152L,130281555L,19264563L,700658875L,137907609L,18494539L,395623327L,21790394L,90326878L,80538516L,95371535L,97345820L,21927384L,271668119L,29457538L,90078985L,18572379L,15408608L,19075157L,30915682L,116181443L,106560372L,185142473L,23869445L,15474719L,44878205L,109336516L,114012899L,17135719L,20947963L,462746293L,364500850L,54786833L,2467791L,37771125L,23371988L,137891415L,28384755L,93126461L,18405431L,104846169L,121844228L,21049538L,136361303L,275698163L,58409701L,39681564L,27925075L,33452323L,98942778L,89563994L,110064618L,105080450L,25347256L,111763152L,47262272L,56576500L,74125349L,102508723L,300306442L,145547583L,26042520L,92669026L,143190996L,95982570L,149639497L,92354987L,22494444L,90413142L,126672596L,18511446L,71976356L,280389436L,33794758L,106943618L,19691977L,472130359L,17983275L,61999562L,387770230L,19924969L,19738343L,31001455L,229576021L,69016358L,15735744L,331651546L,29840295L,16410784L,17471644L,108572494L,113062241L,117119923L,24152639L,113501740L,287339019L,395640448L,135950275L,104057686L,22525435L,98279452L,22812925L,107860844L,22456000L,19287786L,19846862L,150232704L,115626888L,120629600L,27466276L,85179999L,15492791L,24055650L,75284703L,151952113L,138099165L,21154394L,93343294L,238685814L,41250607L,35002594L,347780279L,19148708L,48085452L,19287386L,344183693L,11022062L,68129837L,260328679L,19707724L,17221180L,17625499L,84766621L,23979166L,46110982L,113346432L,78323610L,14399483L,37650996L,94040214L,21331614L,20747271L,304868426L,133023762L,23719018L,109106279L,1115541L,40362826L,200162728L,106596379L,108296252L,91418478L,124434769L,498373219L,112972274L,73102938L,108763450L,71268489L,107930954L,41387326L,31499186L,153483686L,20080565L,242772411L,17272896L,26721071L,49575238L,102331809L,123485906L,51202796L,20591232L,60665070L,50627903L,41655339L,259461466L,23729656L,372982540L,188265664L,137708505L,19300855L,56814021L,20167026L,65881119L,29060498L,47386322L,23112448L,97346257L,513526221L,119748437L,33410725L,27021870L,24882313L,182922805L,25919983L,21803049L,105189289L,30581807L,98006577L,89532927L,92664257L,87640133L,211551305L,285174785L,67185748L,182797278L,130788967L,37619162L,32351745L,50096916L,18038269L,21955562L,95801537L,92254129L,76135448L,41062783L,16788305L,25861473L,22369058L,20749005L,110684256L,323125168L,22613668L,73155887L,19568471L,479652972L,74703549L,590023095L,16634192L,149053141L,102523253L,145672705L,90258110L,25538934L,353775627L,126038665L,79548435L,23302228L,45054066L,86615900L,140895079L,11522502L,115161928L,17291790L,17440047L,278323287L,254207644L,23930568L,15263824L,15563278L,105784684L,69236960L,112905618L,135101703L,61182507L,84120144L,26579816L,54477702L,20792739L,108278646L,281590845L,122213971L,158455848L,353393447L,205647306L,158670254L,103977030L,19040188L,370142654L,59124929L,174605235L,17222256L,39524879L,29439701L,19039286L,123920470L,12199802L,98908415L,15933910L,24120574L,102144287L,42103656L,87066107L,23111195L,59745365L,15718060L,52813110L,32857084L,118653389L,44651827L,129159162L,71042553L,21946180L,92131243L,19249467L,59623019L,100050926L,44651794L,451376490L,73134924L,18314048L,155849609L,142638317L,28187723L,31420124L,117489911L,478782881L,16818325L,262355899L,30479730L,24741563L,79589040L,78011502L,88636693L,1043488980L,36648618L,113514650L,95755978L,47165741L,77491350L,21946088L,44326631L,44511031L,27375704L,35307897L,58242468L,257455890L,24866239L,568624075L,86374551L,208080583L,104344803L,16028862L,18278499L,72820292L,373854960L,475859683L,16197836L,25556731L,21681809L,59846715L,18313732L,381466448L,96529840L,92404363L,116686210L,19268763L,36930961L,122122727L,24415336L,165406658L,52881899L,35658347L,17968456L,20341586L,20685935L,581235589L,172466528L,62759614L,150291315L,348379865L,422182647L,31799873L,63478659L,15317676L,21929316L,458240984L,175186145L,17985145L,142446660L,22798667L,26475758L,17133807L,18465637L,110532476L,58136427L,30010398L,29490604L,83897241L,424959867L,193500676L,16089141L,12595982L,102038852L,22458168L,30240160L,1022671L,18513696L,118173483L,53489268L,56902930L,60723854L,147544652L,51737347L,138097555L,123836276L,18694815L,28979588L,17778677L,45727016L,21436079L,62239850L,106633246L,68996266L,165417069L,61321496L,96104949L,46078907L,119137971L,82408033L,16910471L,14591760L,17064593L,200358354L,146192830L,30791511L,303873801L,22966882L,38050755L,22123351L,156191516L,50680206L,29152131L,68077689L,15096517L,15785713L,29826821L,24052927L,129747688L,221475783L,64826309L,23427062L,213145119L,185642302L,19736218L,108919038L,130514875L,51737153L,225029801L,24052852L,45382510L,31373230L,15564428L,48291142L,38461682L,31218931L,77122885L,280491424L,34182484L,23929403L,22767951L,33252220L,35751647L,17187958L,26961265L,178681667L,107690094L,124892476L,182520387L,38190358L,183239662L,73224426L,21190774L,114898117L,106136306L,203957172L,29720215L,21136909L,43408578L,19598151L,42676565L,16688718L,19126160L,195753199L,52432657L,102907287L,8351992L,19858297L,121863282L,61627026L,34819995L,109231670L,474050866L,94256637L,58442188L,244143740L,49760514L,137904193L,279121988L,104180520L,110792003L,64623707L,19414815L,95525828L,189677310L,22582248L,115704935L,37358186L,24375720L,16340891L,250314584L,184741616L,17188483L,64405581L,97286604L,24238036L,23746948L,225917381L,81474436L,27636889L,102716344L,183915151L,22690835L,34710788L,129053210L,72390234L,95455303L,138045241L,16027675L,81898529L,123300224L,107739006L,17845167L,414260578L,42728901L,16444310L,29420835L,49577249L,124931946L,27482017L,247143438L,148810980L,34649754L,20359779L,24647812L,34357707L};
				
		new GraphBuilder().buildGraph(Arrays.asList(laPerlaFashio2800));
	}
	
	private class Tweet
	{
		private class User
		{
			String screen_name;
		}
		
		User user;
		Tweet retweeted_status;
		
	}
	
	
	
	public void buildGraph(List<Long> usersIds) throws UnknownHostException
	{
		TwitterFacade twitterFacde = TwitterFacadeFactory.getTwitterFacade();
		twitterFacde.getDescriptions(usersIds); //this downloads 100 profiles  per request and it's used to get screen_name from id
		
		List<String> jsonTweets = new ArrayList<String>();
		String[] screenName2GraphId = new String[usersIds.size()];
		for (int i = 0; i < usersIds.size(); i++)
		{
			System.out.println("getting tweets for user "+i+"/"+usersIds.size());
			long userId = usersIds.get(i);
			try
			{
				String screenName = twitterFacde.getScreenName(userId);			
				screenName2GraphId[i] = screenName;
				jsonTweets.addAll(twitterFacde.getUpTo200Tweets(userId));
			}
			catch (TwitterException e)
			{
				System.out.println("Error with user with id "+userId);
			}		
		}
		
		int[][] retweetGraph = new int[usersIds.size()][usersIds.size()];
		for (int i = 0; i < retweetGraph.length; i++)
		{
			for (int j = 0; j < retweetGraph.length; j++)
			{
				retweetGraph[i][j] = 0;
			}
		}
		
		Gson gson = new Gson();
		int tweetsCount = 0;
		int tweetsTot = jsonTweets.size();
		for (String jsonTweet : jsonTweets)
		{
			System.out.println("analyzing tweet "+(tweetsCount++)+"/"+tweetsTot);
			Tweet tweet = gson.fromJson(jsonTweet, Tweet.class);
			if (tweet.retweeted_status!=null)
			{
				String author = tweet.user.screen_name;
				String reweetedAuthor = tweet.retweeted_status.user.screen_name;
				int authorGraphId = getUserGraphId(screenName2GraphId, author);
				int retweetedAuthorGraphId = getUserGraphId(screenName2GraphId, reweetedAuthor);
				if (retweetedAuthorGraphId!=-1)
					retweetGraph[retweetedAuthorGraphId][authorGraphId] = ( retweetGraph[retweetedAuthorGraphId][authorGraphId] +1);
			}
		}
		
		
		printGraphMatrix(retweetGraph);
		
		for (int i=0; i<screenName2GraphId.length; i++)
			System.out.println(i+" - "+screenName2GraphId[i]);
	}
	
	
	private int getUserGraphId(String[] screenName2GraphId, String user)
	{
		for (int i = 0; i < screenName2GraphId.length; i++)
		{
			if (screenName2GraphId[i].equals(user))
				return i;
		}
		return -1;
	}
	
	
	private void printGraphMatrix(int[][] matrix)
	{
		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
		}
	}
}
