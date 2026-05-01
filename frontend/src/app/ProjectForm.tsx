export default function ProjectForm() {
    return (
        <div>
            <h4>Add Project</h4>

            <form>
                <label>Project Name:
                    <input type="text" name="projectName" />
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Add Project</button>
            </form>

            <h4>link project to company</h4>

            <h4>link project to person</h4>


        </div>
    );
}